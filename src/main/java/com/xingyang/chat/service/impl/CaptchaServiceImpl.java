package com.xingyang.chat.service.impl;

import com.google.code.kaptcha.Producer;
import com.xingyang.chat.service.CaptchaService;
import com.xingyang.chat.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Captcha Service Implementation
 *
 * @author XingYang
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private RedisService redisService;

    @Value("${captcha.expiration:5}")
    private long captchaExpiration;

    private static final String CAPTCHA_CODE_KEY = "captcha:code:";

    @Override
    public BufferedImage generateCaptcha(String uuid) {
        log.info("Generating captcha for UUID: {}", uuid);
        
        // Generate captcha text
        String captchaText = captchaProducer.createText();
        log.info("Generated captcha text: {}", captchaText);
        
        // Store the captcha text in Redis with expiration
        String key = CAPTCHA_CODE_KEY + uuid;
        boolean stored = redisService.setCacheObject(key, captchaText, captchaExpiration, TimeUnit.MINUTES);
        log.info("Stored captcha in Redis with key: {}, expiration: {} minutes, success: {}", 
                key, captchaExpiration, stored);
        
        // Generate the captcha image using the text
        BufferedImage image = captchaProducer.createImage(captchaText);
        log.info("Generated captcha image, width: {}, height: {}", 
                  image != null ? image.getWidth() : "null", 
                  image != null ? image.getHeight() : "null");
        
        return image;
    }

    @Override
    public boolean validateCaptcha(String uuid, String captchaCode) {
        if (uuid == null || captchaCode == null) {
            log.warn("UUID or captcha code is null, uuid: {}, captchaCode: {}", uuid, captchaCode);
            return false;
        }
        
        log.info("Validating captcha for UUID: {}, code: {}", uuid, captchaCode);
        
        try {
            // Get the stored captcha code from Redis
            String key = CAPTCHA_CODE_KEY + uuid;
            String storedCaptchaCode = redisService.getCacheObject(key);
            
            if (storedCaptchaCode == null) {
                log.warn("No captcha found in Redis for UUID: {}", uuid);
                return false;
            }
            
            log.info("Retrieved captcha from Redis: {}", storedCaptchaCode);
            
            // Delete the captcha code from Redis after retrieving (one-time use)
            boolean deleted = redisService.deleteObject(key);
            log.info("Deleted captcha from Redis for UUID: {}, success: {}", uuid, deleted);
            
            // Case insensitive comparison - 清理输入验证码中的空格
            captchaCode = captchaCode.trim();
            boolean result = storedCaptchaCode.equalsIgnoreCase(captchaCode);
            log.info("Captcha validation result for UUID {}: {}, stored: '{}', received: '{}'", 
                    uuid, result, storedCaptchaCode, captchaCode);
            
            return result;
        } catch (Exception e) {
            log.error("Error validating captcha: {}", e.getMessage(), e);
            return false;
        }
    }
} 