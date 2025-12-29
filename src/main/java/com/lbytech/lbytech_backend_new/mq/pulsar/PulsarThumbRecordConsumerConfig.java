package com.lbytech.lbytech_backend_new.mq.pulsar;

import org.apache.pulsar.client.api.BatchReceivePolicy;
import org.apache.pulsar.client.api.ConsumerBuilder;
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
                        .maxNumMessages(10)
                        // 设置超时时间（单位：毫秒）
                        .timeout(10000, TimeUnit.MILLISECONDS)
                        .build()
        );
    }
}
