package com.lbytech.lbytech_backend_new.config;

import com.lbytech.lbytech_backend_new.intercepetor.AuthInterceptor;
import com.lbytech.lbytech_backend_new.intercepetor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Mvc相关配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .order(0);  // order越小越先拦截

        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns(
                        "/notebook/**"
                ).order(1);
    }
}
