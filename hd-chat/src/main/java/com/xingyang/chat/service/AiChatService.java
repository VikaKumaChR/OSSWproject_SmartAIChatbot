package com.xingyang.chat.service;

import com.xingyang.chat.model.dto.ChatMessageDto;
import com.xingyang.chat.model.dto.ChatRequestDto;

import java.util.List;
import java.util.function.Consumer;

/**
 * AI Chat Service Interface
 * 
 * @author xingyang
 */
public interface AiChatService {
    
    /**
     * Send chat request and get reply
     *
     * @param request chat request
     * @return AI response message
     */
    ChatMessageDto chat(ChatRequestDto request);
    
    /**
     * Send chat request and get streaming reply
     *
     * @param request chat request
     * @param responseConsumer response consumer for handling streaming response
     */
    void streamChat(ChatRequestDto request, Consumer<String> responseConsumer);
    
    /**
     * Switch chat model
     *
     * @param modelId model ID
     * @return whether switch was successful
     */
    boolean switchModel(String modelId);
} 