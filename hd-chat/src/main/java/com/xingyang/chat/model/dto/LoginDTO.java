package com.xingyang.chat.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Login DTO
 *
 * @author XingYang
 */
@Data
@Schema(description = "Login Request")
public class LoginDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Schema(description = "Remember me")
    private Boolean rememberMe = false;
    
    @Schema(description = "Captcha verification code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Captcha cannot be empty")
    private String captcha;
    
    @Schema(description = "Captcha UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Captcha UUID cannot be empty")
    private String captchaUuid;
} 