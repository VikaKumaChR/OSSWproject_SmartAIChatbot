package com.xingyang.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc Configuration - Ensures controllers are properly scanned for documentation
 *
 * @author XingYang
 */
@Slf4j
@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi allApisApi() {
        return GroupedOpenApi.builder()
                .group("all-apis")
                .packagesToScan("com.xingyang.chat.controller")
                .build();
    }
} 