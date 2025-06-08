package com.xingyang.chat.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Conversation Entity
 *
 * @author XingYang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("conversation")
@Schema(description = "Conversation Entity")
public class Conversation extends BaseEntity {

    @Schema(description = "Conversation title")
    private String title;
    
    @Schema(description = "User ID")
    private Long userId;
    
    @Schema(description = "Model ID")
    private Long modelId;
    
    @Schema(description = "Model configuration (JSON format)")
    private String modelConfig;
    
    @Schema(description = "Conversation status (0: archived, 1: active)")
    private Integer status;
} 