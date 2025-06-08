package com.xingyang.chat.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI Model Entity
 *
 * @author XingYang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("model")
@Schema(description = "AI Model Entity")
public class Model extends BaseEntity {

    @Schema(description = "Model name")
    private String name;
    
    @Schema(description = "Model provider (e.g., OpenAI, DeepSeek)")
    private String provider;
    
    @Schema(description = "Model type (text, image, audio)")
    private String type;
    
    @Schema(description = "API endpoint")
    private String endpoint;
    
    @Schema(description = "Model version")
    private String modelVersion;
    
    @Schema(description = "Token limitation")
    private Integer tokenLimit;
    
    @Schema(description = "Model avatar URL")
    private String avatar;
    
    @Schema(description = "Default parameters (JSON format)")
    private String defaultParams;
    
    @Schema(description = "Model status (0: disabled, 1: enabled)")
    private Integer status;
    
    @Schema(description = "Display order")
    private Integer sort;
} 