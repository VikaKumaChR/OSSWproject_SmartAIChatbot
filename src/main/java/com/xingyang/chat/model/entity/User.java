package com.xingyang.chat.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * User Entity
 *
 * @author XingYang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@Schema(description = "User Entity")
public class User extends BaseEntity {

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Password")
    private String password;

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

    @Schema(description = "User roles")
    @TableField(exist = false)
    private String[] roles;
} 