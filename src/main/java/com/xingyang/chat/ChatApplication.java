package com.xingyang.chat;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * AI Chat Application Main Entry Point
 * 
 * @author XingYang
 */
@OpenAPIDefinition(
    info = @Info(
        title = "HD Chat API Documentation",
        version = "1.0",
        description = "REST API documentation for HD Chat Application"
    )
)
@SpringBootApplication
@MapperScan("com.xingyang.chat.mapper")
@EnableTransactionManagement
@EnableAsync
public class ChatApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatApplication.class, args);
    }
} 