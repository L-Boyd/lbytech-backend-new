package com.lbytech.lbytech_backend_new.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.lbytech.lbytech_backend_new.constant.ThumbConstant;
import com.lbytech.lbytech_backend_new.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 补偿措施：每天2点将 Redis 中的临时点赞数据同步到数据库。
 * 因为极端情况下（如在即将同步临时记录到数据库时，Redis宕机了）系统会将点赞数据暂存在 Redis 中，
 * 本任务会定时检查 Redis 中是否有未同步的数据，并将其补偿到数据库中。
 */
@Component
@Slf4j
public class SyncThumb2DBCompensatoryJob {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SyncThumb2DBJob syncThumb2DBJob;

    @Scheduled(cron = "0 0 2 * * *")    // cron表达式：秒 分 时 日 月 周
    public void run() {
        log.info("开始补偿数据");
        Set<String> thumbKeys = redisTemplate.keys(RedisKeyUtil.getTempThumbKey("") + "*");
        Set<String> needHandleDataSet = new HashSet<>();

        // 处理找到的Redis键，提取时间片标识
        thumbKeys.stream()
                // 过滤掉null值
                .filter(ObjUtil::isNotNull)
                .forEach(thumbKey -> needHandleDataSet.add(
                        // 移除前缀，只保留时间片部分
                        thumbKey.replace(ThumbConstant.TEMP_THUMB_KEY_PREFIX.formatted(""), "")));

        if (CollUtil.isEmpty(needHandleDataSet)) {
            log.info("没有需要补偿的临时数据");
            return;
        }
        // 补偿数据
        for (String date : needHandleDataSet) {
            syncThumb2DBJob.syncThumb2DBByDate(date);
        }
        log.info("临时数据补偿完成");
    }
}
