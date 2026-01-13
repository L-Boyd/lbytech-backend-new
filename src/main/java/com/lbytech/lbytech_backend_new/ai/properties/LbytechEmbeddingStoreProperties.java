package com.lbytech.lbytech_backend_new.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lbytech.rag.postgres")
@Data
public class LbytechEmbeddingStoreProperties {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String table;
}
