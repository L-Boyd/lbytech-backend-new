package com.lbytech.lbytech_backend_new.job;

import com.google.common.collect.Sets;
import com.lbytech.lbytech_backend_new.constant.ThumbConstant;
import com.lbytech.lbytech_backend_new.mq.ThumbEvent;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 点赞对账任务（极端情况下，点赞消息消费失败兜底）
 */
@Service
@Slf4j
public class ThumbReconciliationJob {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired()
    private IThumbRecordService thumbRecordService;

    @Autowired
    private PulsarTemplate<ThumbEvent> pulsarTemplate;

    /**
     * 定时任务入口（每天凌晨2点执行）
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void run() {
        long startTime = System.currentTimeMillis();

        // 获取该分片下的所有用户ID
        Set<String> userEmails = new HashSet<>();
        String pattern = ThumbConstant.USER_THUMB_KEY_PREFIX + "*";
        try (Cursor<String> cursor = redisTemplate.
                scan(ScanOptions.scanOptions()
                        .match(pattern)
                        .count(1000)
                        .build())) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                String userEmail = key.replace(ThumbConstant.USER_THUMB_KEY_PREFIX, "");
                userEmails.add(userEmail);
            }
        }

        // 逐用户比对
        AtomicReference<Integer> diffCount = new AtomicReference<>(0);
        userEmails.forEach(userEmail -> {
            Set<Integer> redisNotebookIds = redisTemplate.opsForHash()
                    .keys(ThumbConstant.USER_THUMB_KEY_PREFIX + userEmail)
                    .stream()
                    .map(obj -> Integer.valueOf(obj.toString()))
                    .collect(Collectors.toSet());
            Set<Integer> mysqlNotebookIds = Optional.ofNullable(
                    thumbRecordService.lambdaQuery()
                            .eq(ThumbRecord::getUserEmail, userEmail)
                            .list()
                    ).orElse(new ArrayList<>())
                    .stream()
                    .map(ThumbRecord::getNotebookId)
                    .collect(Collectors.toSet());

            // 3. 计算差异（Redis有但MySQL无）  
            Set<Integer> diffNotebookIds = Sets.difference(redisNotebookIds, mysqlNotebookIds);
            diffCount.updateAndGet(v -> v + diffNotebookIds.size());

            // 4. 发送补偿事件  
            sendCompensationEvents(userEmail, diffNotebookIds);
        });

        log.info("点赞记录对账任务完成，耗时 {}ms，差异记录数 {}", System.currentTimeMillis() - startTime, diffCount);
    }

    /**
     * 发送补偿事件到Pulsar
     */
    private void sendCompensationEvents(String userEmail, Set<Integer> notebookIds) {
        notebookIds.forEach(notebookId -> {
            ThumbEvent thumbEvent = new ThumbEvent(userEmail, notebookId, ThumbEvent.EventType.INCR, LocalDateTime.now());
            pulsarTemplate.sendAsync("thumb-topic", thumbEvent)
                    .exceptionally(ex -> {
                        log.error("补偿事件发送失败: userEmail={}, notebookId={}", userEmail, notebookId, ex);
                        return null;
                    });
        });
    }
}
