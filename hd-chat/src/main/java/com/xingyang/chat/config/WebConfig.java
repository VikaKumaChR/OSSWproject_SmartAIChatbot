package com.xingyang.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 *
 * @author XingYang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 配置全局CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
                
        // 添加日志以便调试
        System.out.println("Global CORS configuration applied: allowed origins = [http://localhost:3000, http://localhost:3001]");
    }
} 