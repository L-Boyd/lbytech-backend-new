package com.lbytech.lbytech_backend_new.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lbytech.lbytech_backend_new.mapper.NotebookMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.ThumbTypeEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时将 Redis 中的临时点赞数据同步到数据库
 */
@Component
@Slf4j
public class SyncThumb2DBJob {

    @Autowired
    private IThumbRecordService thumbRecordService;

    @Autowired
    private NotebookMapper notebookMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定时任务执行方法 - 每10秒执行一次
     * 功能：将Redis中的临时点赞数据同步到数据库
     * 时间策略：以10秒为时间片，处理上一时间片的数据
     */
    @Scheduled(fixedRate = 10000)
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        DateTime nowDate = DateUtil.date();
        log.debug("开始同步临时数据：{}", nowDate);
        // 如果秒数为0~9 则回到上一分钟的50秒
        int second = (DateUtil.second(nowDate) / 10 - 1) * 10;
        if (second == -10) {
            second = 50;
            // 回到上一分钟
            nowDate = DateUtil.offsetMinute(nowDate, -1);
        }
        String date = DateUtil.format(nowDate, "HH:mm:") + second;
        syncThumb2DBByDate(date);
        log.debug("临时数据同步完成：{}", DateUtil.date());
    }

    /**
     * 将指定时间片的Redis临时点赞数据同步到数据库
     *
     * @param date 时间片标识，格式：HH:mm:ss（如：12:30:00）
     *             处理逻辑：
     *             1. 从Redis获取临时点赞数据
     *             2. 分离点赞和取消点赞记录
     *             3. 批量插入新点赞记录
     *             4. 批量删除取消点赞记录
     *             5. 批量更新笔记本点赞数
     *             6. 异步清理Redis临时数据
     */
    public void syncThumb2DBByDate(String date) {
        // 获取到临时点赞和取消点赞数据  
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(date);

        // 获取所有键值对（用户Email:笔记ID → 点赞类型）
        Map<Object, Object> allTempThumbMap = redisTemplate.opsForHash().entries(tempThumbKey);
        if (CollUtil.isEmpty(allTempThumbMap)) {
            return;
        }

        // 初始化笔记点赞数增量映射表（笔记ID → 点赞数变化量）
        Map<Integer, Integer> notebookThumbCountMap = new HashMap<>();

        // 待插入的点赞记录列表
        ArrayList<ThumbRecord> thumbList = new ArrayList<>();

        LambdaQueryWrapper<ThumbRecord> wrapper = new LambdaQueryWrapper<>();
        // 标记是否需要执行删除操作
        boolean needRemove = false;

        for (Object userEmailNotebookIdObj : allTempThumbMap.keySet()) {
            String userEmailNotebookId = (String) userEmailNotebookIdObj;
            String[] userIdAndBlogId = userEmailNotebookId.split(":");
            String userEmail = userIdAndBlogId[0];
            Integer notebookId = Integer.valueOf(userIdAndBlogId[1]);

            // -1 取消点赞，1 点赞
            Integer thumbType = Integer.valueOf(allTempThumbMap.get(userEmailNotebookId).toString());
            if (thumbType == ThumbTypeEnum.INCR.getValue()) {
                // 点赞操作：创建新的点赞记录
                ThumbRecord thumbRecord = new ThumbRecord();
                thumbRecord.setUserEmail(userEmail);
                thumbRecord.setNotebookId(notebookId);
                thumbList.add(thumbRecord);
            } else if (thumbType == ThumbTypeEnum.DECR.getValue()) {
                // 取消点赞操作：构建删除条件
                needRemove = true;
                wrapper.or().eq(ThumbRecord::getUserEmail, userEmail).eq(ThumbRecord::getNotebookId, notebookId);
            } else {
                // 异常数据处理：记录警告日志
                if (thumbType != ThumbTypeEnum.NON.getValue()) {
                    log.warn("数据异常：{}", userEmail + "," + notebookId + "," + thumbType);
                }
                continue;
            }
            // 计算点赞增量
            notebookThumbCountMap.put(notebookId, notebookThumbCountMap.getOrDefault(notebookId, 0) + thumbType);
        }
        // 批量插入  
        thumbRecordService.saveBatch(thumbList);
        // 批量删除
        if (needRemove) {
            thumbRecordService.remove(wrapper);
        }
        // 批量更新博客点赞量  
        if (!notebookThumbCountMap.isEmpty()) {
            notebookMapper.batchUpdateThumbCount(notebookThumbCountMap);
        }
        // 异步删除  
        Thread.startVirtualThread(() -> {
            redisTemplate.delete(tempThumbKey);
        });
    }
}
