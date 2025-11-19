package com.lbytech.lbytech_backend_new.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端域名（本地8000），生产环境替换为实际前端域名
        config.addAllowedOrigin("http://localhost:8000");
        config.addAllowedOriginPattern("*");
        // 允许所有请求头（如Authorization、Content-Type）
        config.addAllowedHeader("*");
        // 允许所有请求方法（GET、POST、PUT等）
        config.addAllowedMethod("*");
        // 允许携带Cookie（如需登录态）
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有接口都启用跨域配置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
