package com.xingyang.chat.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat Message Data Transfer Object
 * 
 * @author xingyang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    
    /**
     * Message ID
     */
    private String id;
    
    /**
     * Message role: user, assistant, system
     */
    private String role;
    
    /**
     * Message content
     */
    private String content;
    
    /**
     * Message timestamp
     */
    private Long timestamp;
    
    /**
     * Create a user message
     */
    public static ChatMessageDto userMessage(String content) {
        return ChatMessageDto.builder()
                .role("user")
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * Create a system message
     */
    public static ChatMessageDto systemMessage(String content) {
        return ChatMessageDto.builder()
                .role("system")
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * Create an assistant message
     */
    public static ChatMessageDto assistantMessage(String content) {
        return ChatMessageDto.builder()
                .role("assistant")
                .content(content)
                .timestamp(System.currentTimeMillis())
                .build();
    }
} 