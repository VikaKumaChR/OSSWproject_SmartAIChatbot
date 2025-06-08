package com.xingyang.chat.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Email Send DTO
 *
 * @author XingYang
 */
@Data
@Schema(description = "Email Send Request")
public class EmailSendDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "Email address", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "3266303694@qq\\.com$", message = "Please enter a valid QQ email address")
    private String email;

    @Schema(description = "Verification type (register, reset)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Type cannot be empty")
    @Pattern(regexp = "^(register|reset)$", message = "Type must be either 'register' or 'reset'")
    private String type;
} 