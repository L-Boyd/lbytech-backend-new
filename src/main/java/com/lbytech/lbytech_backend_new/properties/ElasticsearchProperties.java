package com.lbytech.lbytech_backend_new.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lbytech.elasticsearch")
@Data
public class ElasticsearchProperties {

    private String host;
}
