package com.lbytech.lbytech_backend_new;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = "classpath:environmentVariables.env")
public class LbytechBackendNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbytechBackendNewApplication.class, args);
    }

}
