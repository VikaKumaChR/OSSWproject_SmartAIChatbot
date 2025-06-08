package com.xingyang.chat.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * User Update DTO
 *
 * @author XingYang
 */
@Data
@Schema(description = "User Update Request")
public class UserUpdateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Avatar URL")
    private String avatar;

    @Schema(description = "Email")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Phone number")
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "Invalid phone number format")
    private String phone;
} 