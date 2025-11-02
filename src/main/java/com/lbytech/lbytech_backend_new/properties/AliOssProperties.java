package com.lbytech.lbytech_backend_new.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ali.oss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyID;
    private String accessKeySecret;
    private String bucketName;

}
