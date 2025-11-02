package com.lbytech.lbytech_backend_new.config;

import com.lbytech.lbytech_backend_new.properties.AliOssProperties;
import com.lbytech.lbytech_backend_new.util.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean//当容器中没有这个Bean的情况下，才会创建这个Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云OSS工具类对象：{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                                        aliOssProperties.getAccessKeyID(),
                                        aliOssProperties.getAccessKeySecret(),
                                        aliOssProperties.getBucketName());
    }
}
