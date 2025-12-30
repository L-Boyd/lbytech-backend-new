package com.lbytech.lbytech_backend_new.mq.pulsar;

import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lbytech.lbytech_backend_new.mapper.NotebookMapper;
import com.lbytech.lbytech_backend_new.mq.ThumbEvent;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.common.schema.SchemaType;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PulsarThumbRecordConsumer {

    private final NotebookMapper notebookMapper;
    private final IThumbRecordService thumbRecordService;

    // 批量处理配置  
    @PulsarListener(
            subscriptionName = "thumb-subscription",
            topics = "thumb-topic",
            schemaType = SchemaType.JSON,
            batch = true,
            consumerCustomizer = "pulsarThumbRecordConsumerConfig"
    )
    @Transactional(rollbackFor = Exception.class)
    public void processBatch(List<Message<ThumbEvent>> messages) {
        log.info("ThumbConsumer processBatch: {}", messages.size());

        // notebookId, 点赞数
        Map<Integer, Integer> countMap = new ConcurrentHashMap<>();
        List<ThumbRecord> thumbRecords = new ArrayList<>();

        // 并行处理消息  
        LambdaQueryWrapper<ThumbRecord> wrapper = new LambdaQueryWrapper<>();

        AtomicReference<Boolean> needRemove = new AtomicReference<>(false);

        // 提取事件并过滤无效消息  
        List<ThumbEvent> events = messages.stream()
                .map(Message::getValue)
                .filter(Objects::nonNull)
                .toList();


        // 按(userEmail, notebookId)分组，并获取每个分组的最新事件
        Map<Pair<String, Integer>, ThumbEvent> latestEvents = events.stream()
                .collect(Collectors.groupingBy(
                        e -> Pair.of(e.getUserEmail(), e.getNotebookId()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    // 按时间升序排序，取最后一个作为最新事件  
                                    list.sort(Comparator.comparing(ThumbEvent::getEventTime));
                                    if (list.size() % 2 == 0) {
                                        return null;
                                    }
                                    return list.get(list.size() - 1);
                                }
                        )
                ));

        latestEvents.forEach((userNotebookPair, event) -> {
            if (event == null) {
                return;
            }
            ThumbEvent.EventType finalAction = event.getType();

            if (finalAction == ThumbEvent.EventType.INCR) {
                countMap.merge(event.getNotebookId(), 1, Integer::sum);
                ThumbRecord thumbRecord = new ThumbRecord();
                thumbRecord.setNotebookId(event.getNotebookId());
                thumbRecord.setUserEmail(event.getUserEmail());
                thumbRecords.add(thumbRecord);
            } else {
                needRemove.set(true);
                wrapper.or()
                        .eq(ThumbRecord::getUserEmail, event.getUserEmail())
                        .eq(ThumbRecord::getNotebookId, event.getNotebookId());
                countMap.merge(event.getNotebookId(), -1, Integer::sum);
            }
        });

        // 批量更新数据库  
        if (needRemove.get()) {
            thumbRecordService.remove(wrapper);
        }
        batchUpdateNotebooks(countMap);
        batchInsertThumbRecords(thumbRecords);
    }

    public void batchUpdateNotebooks(Map<Integer, Integer> countMap) {
        if (!countMap.isEmpty()) {
            notebookMapper.batchUpdateThumbCount(countMap);
        }
    }

    public void batchInsertThumbRecords(List<ThumbRecord> thumbRecords) {
        if (!thumbRecords.isEmpty()) {
            // 分批次插入  
            thumbRecordService.saveBatch(thumbRecords, 5);
        }
    }
}
