package com.lbytech.lbytech_backend_new.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.constant.RedisLuaScriptConstant;
import com.lbytech.lbytech_backend_new.mapper.ThumbRecordMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.LuaStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.service.IUserService;
import com.lbytech.lbytech_backend_new.util.RedisKeyUtil;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("thumbRecordService")
@Slf4j
public class ThumbRecordServiceRedisImpl extends ServiceImpl<ThumbRecordMapper, ThumbRecord> implements IThumbRecordService {

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;
  
    @Override  
    public Boolean thumbNotebook(Integer notebookId) {
        if (notebookId == null) {
            throw new RuntimeException("参数错误");  
        }
        UserVO user = UserHolder.getUser();

        // 计算时间片，使用时间分片策略将点赞操作临时存储在redis中，后续使用定时任务批量处理每个时间片中的数据
        String timeSlice = this.getTimeSlice();
        // Redis Key  
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);
        String userThumbKey = RedisKeyUtil.getUserThumbKey(user.getEmail());
  
        // 执行 Lua 脚本  
        long result = redisTemplate.execute(  
                RedisLuaScriptConstant.THUMB_SCRIPT,
                Arrays.asList(tempThumbKey, userThumbKey),
                user.getEmail(),
                notebookId
        );  
  
        if (LuaStatusEnum.FAIL.getValue() == result) {
            throw new RuntimeException("用户已点赞");  
        }  
  
        // 更新成功才执行  
        return LuaStatusEnum.SUCCESS.getValue() == result;  
    }  
  
    @Override  
    public Boolean unThumbNotebook(Integer notebookId) {
        if (notebookId == null) {
            throw new RuntimeException("参数错误");  
        }
        UserVO user = UserHolder.getUser();

        // 计算时间片  
        String timeSlice = getTimeSlice();  
        // Redis Key  
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);  
        String userThumbKey = RedisKeyUtil.getUserThumbKey(user.getEmail());
  
        // 执行 Lua 脚本  
        long result = redisTemplate.execute(  
                RedisLuaScriptConstant.UNTHUMB_SCRIPT,  
                Arrays.asList(tempThumbKey, userThumbKey),  
                user.getEmail(),
                notebookId
        );  
        // 根据返回值处理结果  
        if (result == LuaStatusEnum.FAIL.getValue()) {  
            throw new RuntimeException("用户未点赞");  
        }  
        return LuaStatusEnum.SUCCESS.getValue() == result;  
    }  
  
    private String getTimeSlice() {  
        DateTime nowDate = DateUtil.date();
        // 获取到当前时间前最近的整数秒，比如当前 11:20:23 ，获取到 11:20:20  
        return DateUtil.format(nowDate, "HH:mm:") + (DateUtil.second(nowDate) / 10) * 10;  
    }  
  
    @Override  
    public Boolean hasThumb(String userEmail, Integer notebookId) {
        return redisTemplate.opsForHash().hasKey(RedisKeyUtil.getUserThumbKey(userEmail), notebookId.toString());
    }  
}
