package com.xingyang.chat.service;

import java.awt.image.BufferedImage;

/**
 * Captcha Service Interface
 *
 * @author XingYang
 */
public interface CaptchaService {
    
    /**
     * Generate a captcha image
     *
     * @param uuid Unique identifier for the captcha
     * @return Captcha image
     */
    BufferedImage generateCaptcha(String uuid);
    
    /**
     * Validate the captcha code
     *
     * @param uuid Captcha identifier
     * @param code User input captcha code
     * @return Whether validation is successful
     */
    boolean validateCaptcha(String uuid, String code);
} 