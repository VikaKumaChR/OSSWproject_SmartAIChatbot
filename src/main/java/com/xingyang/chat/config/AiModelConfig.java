package com.xingyang.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.model")
public class AiModelConfig {

    /**
     * 阿里云百联API密钥
     */
    private String apiKey;
    
    /**
     * 模型ID，默认为千问模型
     */
    private String modelId = "qwen-plus";
    
    /**
     * API端点
     */
    private String endpoint = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    
    /**
     * 最大令牌数
     */
    private Integer maxTokens = 4000;
    
    /**
     * 温度参数，控制回答的随机性
     */
    private Double temperature = 0.7;
    
    /**
     * 配置OpenAI格式的聊天模型Bean
     * 使用兼容OpenAI接口的阿里云服务
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .baseUrl(endpoint)
                .apiKey(apiKey)
                .modelName(modelId)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
} 