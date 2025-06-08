package com.xingyang.chat.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat Request Data Transfer Object
 * 
 * @author xingyang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {
    
    /**
     * Chat message history list
     */
    private List<ChatMessageDto> messages;
    
    /**
     * Selected model ID
     */
    private String model;
    
    /**
     * Temperature parameter, controls randomness
     */
    private Double temperature;
    
    /**
     * Maximum tokens for generation
     */
    private Integer max_tokens;
    
    /**
     * Enable deep thinking mode
     */
    private Boolean deep_thinking;
} 