package com.xingyang.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * AI Model Configuration
 * 
 * @author xingyang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai.model")
public class AiModelConfig {

    /**
     * Alibaba Bailing API key
     */
    private String apiKey;
    
    /**
     * Model ID, default is Qwen-plus
     */
    private String modelId = "qwen-plus";
    
    /**
     * API endpoint
     */
    private String endpoint = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    
    /**
     * Maximum tokens for generation
     */
    private Integer maxTokens = 4000;
    
    /**
     * Temperature parameter, controls randomness
     */
    private Double temperature = 0.7;
    
    /**
     * Configure OpenAI format chat model bean
     * Using Alibaba Cloud service with OpenAI compatible interface
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        // Create a dummy implementation that will be replaced by direct API calls
        return messages -> {
            throw new UnsupportedOperationException("This is a placeholder implementation. Use direct API calls instead.");
        };
    }
} 