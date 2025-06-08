package com.xingyang.chat.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Email Verification DTO
 *
 * @author XingYang
 */
@Data
@Schema(description = "Email Verification Request")
public class EmailVerifyDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "Email address", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[1-9]\\d{4,}@qq\\.com$", message = "Please enter a valid QQ email address")
    private String email;

    @Schema(description = "Verification code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Verification code cannot be empty")
    @Size(min = 6, max = 6, message = "Verification code must be 6 digits")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must contain only digits")
    private String code;
} 