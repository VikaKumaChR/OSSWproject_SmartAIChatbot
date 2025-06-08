package com.xingyang.chat.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * User View Object
 *
 * @author XingYang
 */
@Data
@Schema(description = "User View Object")
public class UserVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Nickname")
    private String nickname;

    @Schema(description = "Avatar URL")
    private String avatar;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "User status (0: disabled, 1: enabled)")
    private Integer status;

    @Schema(description = "Last login time")
    private Date lastLoginTime;

    @Schema(description = "Creation time")
    private LocalDateTime createTime;

    @Schema(description = "User roles")
    private String[] roles;
} 