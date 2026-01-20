package com.lbytech.lbytech_backend_new.mq.rocket.testConsutmer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(
        topic = "test-topic1",
        consumerGroup = "lbytech-consumer-group"
)
@Component
public class TestConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("收到消息：" + s);
    }
}
