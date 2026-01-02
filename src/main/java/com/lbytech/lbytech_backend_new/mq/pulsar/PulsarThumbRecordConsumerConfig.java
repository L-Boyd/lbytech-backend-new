package com.lbytech.lbytech_backend_new.mq.pulsar;

import org.apache.pulsar.client.api.BatchReceivePolicy;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.RedeliveryBackoff;
import org.apache.pulsar.client.impl.MultiplierRedeliveryBackoff;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.annotation.PulsarListenerConsumerBuilderCustomizer;

import java.util.concurrent.TimeUnit;

/**
 * 批量处理策略配置
 */
@Configuration
public class PulsarThumbRecordConsumerConfig<T> implements PulsarListenerConsumerBuilderCustomizer<T> {
    @Override
    public void customize(ConsumerBuilder<T> consumerBuilder) {
        consumerBuilder.batchReceivePolicy(
                BatchReceivePolicy.builder()
                        // 每次处理 1000 条
                        .maxNumMessages(1000)
                        // 设置超时时间（单位：毫秒）:收集1000条马上处理，10秒没有新消息也会处理
                        .timeout(10000, TimeUnit.MILLISECONDS)
                        .build()
        );
    }

    // 配置 NACK 重试策略
    @Bean
    public RedeliveryBackoff negativeAckRedeliveryBackoff() {
        return MultiplierRedeliveryBackoff.builder()
                // 初始延迟 1 秒(当消息首次处理失败并NACK时，会等待1秒后进行第一次重试)
                .minDelayMs(1000)
                // 最大延迟 60 秒(无论重试多少次，单次延迟时间不会超过60秒)
                .maxDelayMs(60_000)
                // 每次重试延迟倍数(每次重试延迟时间会乘以这个倍数，例如第一次重试延迟1秒，第二次重试延迟2秒，第三次重试延迟4秒，以此类推)
                .multiplier(2)
                .build();
    }

    // 配置 ACK 超时重试策略
    @Bean
    public RedeliveryBackoff ackTimeoutRedeliveryBackoff() {
        return MultiplierRedeliveryBackoff.builder()
                // 初始延迟 5 秒
                .minDelayMs(5000)
                // 最大延迟 300 秒
                .maxDelayMs(300_000)
                .multiplier(3)
                .build();
    }

    // 死信策略
    @Bean
    public DeadLetterPolicy deadLetterPolicy() {
        return DeadLetterPolicy.builder()
                // 最大重试次数(当消息重试次数超过3次时，会将消息发送到死信队列)
                .maxRedeliverCount(3)
                // 死信主题名称
                .deadLetterTopic("thumb-dlq-topic")
                .build();
    }


}
