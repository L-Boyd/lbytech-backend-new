package com.lbytech.lbytech_backend_new;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lbytech.lbytech_backend_new.mapper")
public class LbytechBackendNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbytechBackendNewApplication.class, args);
    }

}
