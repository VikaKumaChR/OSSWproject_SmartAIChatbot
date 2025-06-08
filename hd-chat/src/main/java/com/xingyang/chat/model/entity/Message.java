package com.xingyang.chat.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message Entity
 *
 * @author XingYang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message")
@Schema(description = "Message Entity")
public class Message extends BaseEntity {

    @Schema(description = "Conversation ID")
    private Long conversationId;
    
    @Schema(description = "Message content")
    private String content;
    
    @Schema(description = "Message role (user/assistant/system)")
    private String role;
    
    @Schema(description = "Token count")
    private Integer tokenCount;
    
    @Schema(description = "Message type (text/image/audio)")
    private String type;
    
    @Schema(description = "Parent message ID")
    private Long parentId;
    
    @Schema(description = "Message metadata (JSON format)")
    private String metadata;
    
    @Schema(description = "Model ID used for this message")
    private Long modelId;
} 