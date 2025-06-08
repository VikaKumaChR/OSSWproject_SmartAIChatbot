package com.xingyang.chat.service;

/**
 * Email Service Interface
 *
 * @author XingYang
 */
public interface EmailService {

    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param type 验证码类型（register-注册，reset-重置密码）
     * @return 是否发送成功
     */
    boolean sendCode(String email, String type);

    /**
     * 验证邮箱验证码
     * @param email 邮箱地址
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyCode(String email, String code);
} 