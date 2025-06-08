package com.xingyang.chat.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Register DTO
 *
 * @author XingYang
 */
@Data
@Schema(description = "Register Request")
public class RegisterDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "Username", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores and hyphens")
    private String username;

    @Schema(description = "Password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @Schema(description = "Confirm password", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Phone number")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "Invalid phone number format")
    private String phone;
    
    @Schema(description = "Captcha", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Captcha cannot be empty")
    private String captcha;

    @Schema(description = "Captcha UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Captcha UUID cannot be empty")
    private String captchaUuid;
}