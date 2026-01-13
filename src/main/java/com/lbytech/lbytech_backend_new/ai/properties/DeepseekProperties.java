package com.lbytech.lbytech_backend_new.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lbytech.model.deepseek")
@Data
public class DeepseekProperties {

    private String baseUrl;
    private String apiKey;
    private String modelName ;
    private boolean logRequests;
    private boolean logResponses;
    private int maxTokens;
}
