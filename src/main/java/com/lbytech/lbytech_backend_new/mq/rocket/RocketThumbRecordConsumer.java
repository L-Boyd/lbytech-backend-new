package com.lbytech.lbytech_backend_new.mq.rocket;

import com.lbytech.lbytech_backend_new.es.INotebookEsService;
import com.lbytech.lbytech_backend_new.mq.ThumbEvent;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(
        topic = "thumb-topic",
        consumerGroup = "thumb-consumer-group"
)
@Component
public class RocketThumbRecordConsumer implements RocketMQListener<ThumbEvent> {

    @Autowired
    private INotebookService notebookService;

    @Autowired
    private IThumbRecordService thumbRecordService;

    @Autowired
    private INotebookEsService notebookEsService;

    @Override
    public synchronized void onMessage(ThumbEvent thumbEvent) {
        Thread.startVirtualThread(() -> {
            ThumbEvent.EventType type = thumbEvent.getType();
            if (type == ThumbEvent.EventType.INCR) {
                // 点赞
                // 新增点赞记录
                ThumbRecord thumbRecord = new ThumbRecord();
                thumbRecord.setNotebookId(thumbEvent.getNotebookId());
                thumbRecord.setUserEmail(thumbEvent.getUserEmail());
                thumbRecordService.save(thumbRecord);

                // 更新笔记点赞数
                notebookService.updateThumbCount(thumbEvent.getNotebookId(), 1);
                // 更新es
                notebookEsService.updateThumbCount(thumbEvent.getNotebookId(), notebookService.getById(thumbEvent.getNotebookId()).getThumbCount());
            } else {
                // 取消点赞
                // 删除点赞记录
                thumbRecordService.lambdaUpdate()
                        .eq(ThumbRecord::getNotebookId, thumbEvent.getNotebookId())
                        .eq(ThumbRecord::getUserEmail, thumbEvent.getUserEmail())
                        .remove();

                // 更新笔记点赞数
                notebookService.updateThumbCount(thumbEvent.getNotebookId(), -1);
                // 更新es
                notebookEsService.updateThumbCount(thumbEvent.getNotebookId(), notebookService.getById(thumbEvent.getNotebookId()).getThumbCount());
            }
        });
    }
}
