package com.xingyang.chat.controller;

import com.xingyang.chat.model.dto.ChatMessageDto;
import com.xingyang.chat.model.dto.ChatRequestDto;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.service.AiChatService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chat Controller
 * 
 * @author xingyang
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@Tag(name = "Chat API", description = "Chat with AI models")
public class ChatController {

    @Autowired
    private AiChatService aiChatService;
    
    // Store SSE emitters for streaming responses
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * Chat with AI (non-streaming)
     *
     * @param request chat request
     * @return AI response
     */
    @PostMapping
    @Operation(summary = "Chat with AI", description = "Send a chat request and get a response")
    public Result<ChatMessageDto> chat(@RequestBody ChatRequestDto request) {
        log.info("Received chat request, model: {}, messages count: {}", 
                request.getModel(), request.getMessages() != null ? request.getMessages().size() : 0);
        try {
            ChatMessageDto response = aiChatService.chat(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("Error in chat controller", e);
            return Result.error(500, "Chat processing error: " + e.getMessage());
        }
    }

    /**
     * Stream chat with AI using Server-Sent Events (SSE)
     *
     * @param request chat request
     * @return Server-Sent Events stream
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream chat with AI", description = "Send a chat request and get a streaming response using Server-Sent Events")
    public SseEmitter streamChat(@RequestBody ChatRequestDto request) {
        log.info("Received chat request for stream API, model: {}, messages count: {}", 
                request.getModel(), request.getMessages() != null ? request.getMessages().size() : 0);
        
        // Create SSE emitter with a timeout
        SseEmitter emitter = new SseEmitter(180000L); // 3 minutes timeout
        
        try {
            // Set up SSE emitter completion callbacks
            emitter.onCompletion(() -> log.info("SSE completed"));
            emitter.onTimeout(() -> log.warn("SSE timeout"));
            emitter.onError((ex) -> log.error("SSE error", ex));
            
            // Start streaming the response
            aiChatService.streamChat(request, token -> {
                try {
                    // Send each token as a plain text SSE event
                    emitter.send(token, MediaType.TEXT_PLAIN);
                } catch (IOException e) {
                    log.error("Error sending SSE event", e);
                    emitter.completeWithError(e);
                }
            });
            
            // Complete the emitter when streaming is done
            emitter.complete();
            
        } catch (Exception e) {
            log.error("Error in stream chat controller", e);
            emitter.completeWithError(e);
        }
        
        return emitter;
    }
    
    /**
     * Switch the AI model
     *
     * @param modelId ID of the model to switch to
     * @return success or error response
     */
    @PostMapping("/model/{modelId}")
    @Operation(summary = "Switch AI model", description = "Change the AI model being used")
    public Result<Boolean> switchModel(
            @Parameter(description = "Model ID to switch to") 
            @PathVariable String modelId) {
        log.info("Switching model to: {}", modelId);
        try {
            boolean success = aiChatService.switchModel(modelId);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error(500, "Failed to switch model");
            }
        } catch (Exception e) {
            log.error("Error switching model", e);
            return Result.error(500, "Error switching model: " + e.getMessage());
        }
    }
} 