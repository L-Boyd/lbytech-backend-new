package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.constant.RedisLuaScriptConstant;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.mapper.ThumbRecordMapper;
import com.lbytech.lbytech_backend_new.mq.ThumbEvent;
import com.lbytech.lbytech_backend_new.pojo.Enum.LuaStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.RedisKeyUtil;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("thumbRecordService")
@Slf4j
@RequiredArgsConstructor
public class ThumbRecordServiceMQImpl extends ServiceImpl<ThumbRecordMapper, ThumbRecord> implements IThumbRecordService {

    private final StringRedisTemplate redisTemplate;

    private final PulsarTemplate<ThumbEvent> pulsarTemplate;

    @Override
    public Boolean thumbNotebook(Integer notebookId) {
        if (notebookId == null) {
            throw new BusinessException(StatusCodeEnum.FAIL, "参数错误");
        }
        UserVO user = UserHolder.getUser();
        String userEmail = user.getEmail();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(userEmail);

        // 执行 Lua 脚本，点赞存入 Redis  
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.THUMB_SCRIPT_MQ,
                List.of(userThumbKey),
                notebookId.toString()
        );
        // 检查 Lua 脚本执行结果
        if (result == LuaStatusEnum.NOTEBOOK_NOT_EXIST.getValue()) {
            throw new BusinessException(StatusCodeEnum.FAIL, "笔记不存在");
        }
        if (result == LuaStatusEnum.FAIL.getValue()) {
            throw new BusinessException(StatusCodeEnum.FAIL, "用户已点赞");
        }

        ThumbEvent thumbEvent = ThumbEvent.builder()
                .notebookId(notebookId)
                .userEmail(userEmail)
                .type(ThumbEvent.EventType.INCR)
                .eventTime(LocalDateTime.now())
                .build();
        pulsarTemplate.sendAsync("thumb-topic", thumbEvent)
                .exceptionally(ex -> {
                    redisTemplate.opsForHash().delete(userThumbKey, notebookId.toString(), true);
                    log.error("点赞事件发送失败: userEmail={}, notebookId={}", userEmail, notebookId, ex);
                    return null;
                });

        return true;
    }

    @Override
    public Boolean unThumbNotebook(Integer notebookId) {
        if (notebookId == null) {
            throw new BusinessException(StatusCodeEnum.FAIL, "参数错误");
        }
        UserVO user = UserHolder.getUser();
        String userEmail = user.getEmail();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(userEmail);

        // 执行 Lua 脚本，点赞记录从 Redis 删除  
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.UNTHUMB_SCRIPT_MQ,
                List.of(userThumbKey),
                notebookId.toString()
        );
        if (result == LuaStatusEnum.FAIL.getValue()) {
            throw new BusinessException(StatusCodeEnum.FAIL, "用户未点赞");
        }

        ThumbEvent thumbEvent = ThumbEvent.builder()
                .notebookId(notebookId)
                .userEmail(userEmail)
                .type(ThumbEvent.EventType.DECR)
                .eventTime(LocalDateTime.now())
                .build();
        pulsarTemplate.sendAsync("thumb-topic", thumbEvent)
                .exceptionally(ex -> {
                    redisTemplate.opsForHash().put(userThumbKey, notebookId.toString(), true);
                    log.error("点赞事件发送失败: userEmail={}, notebookId={}", userEmail, notebookId, ex);
                    return null;
                });

        return true;
    }

    @Override
    public Boolean hasThumb(String userEmail, Integer notebookId) {
        return redisTemplate.opsForHash()
                .hasKey(RedisKeyUtil.getUserThumbKey(userEmail), notebookId.toString());
    }

}
