package com.lbytech.lbytech_backend_new.mq.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RocketMQProducerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 测试同步发送消息
     */
    @Test
    public void testSyncSend() {
        // 同步发送消息
        String topic = "test-topic1";
        String message = "Hello, RocketMQ! 同步发送消息";
        SendResult sendResult = rocketMQTemplate.syncSend(topic, message);
        System.out.println(sendResult);
    }

    /**
     * 测试异步发送消息
     */
    @Test
    public void testAsyncSend() {
        // 异步发送消息
        String topic = "test-topic2";
        String message = "Hello, RocketMQ! 异步发送消息";
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送成功：" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步发送异常：" + throwable);
            }
        });
    }

    /**
     * 测试单向发送消息（只管发送，不管消息发送是否成功）
     */
    @Test
    public void testOnewaySend() {
        // 单向发送消息
        String topic = "test-topic3";
        String message = "Hello, RocketMQ! 单向发送消息";
        rocketMQTemplate.sendOneWay(topic, message);
    }

}
