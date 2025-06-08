package com.xingyang.chat.service.impl;

import com.xingyang.chat.exception.BusinessException;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * Email Service Implementation
 *
 * @author XingYang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.nickname:AI Assistant}")
    private String emailNickname;

    // Verification code expiration time (seconds)
    private static final long CODE_EXPIRE_SECONDS = 600; // 10分钟

    // Redis key prefix
    private static final String EMAIL_CODE_PREFIX = "email:code:";

    // QQ email regex pattern
    private static final Pattern QQ_EMAIL_PATTERN = Pattern.compile("^[1-9]\\d{4,}@qq\\.com$");

    @Override
    public boolean sendCode(String email, String type) {
        // Validate email format
        if (!QQ_EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(Result.ResultCode.PARAM_ERROR.getCode(), "Please enter a valid QQ email address");
        }

        // Generate 6-digit verification code
        String code = generateVerificationCode();

        // Store code in Redis with 5-minute expiration
        String redisKey = EMAIL_CODE_PREFIX + type + ":" + email;
        redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // Send email
        try {
            sendEmail(email, code, type);
            log.info("Email verification code sent to: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email verification code: {}", e.getMessage(), e);
            throw new BusinessException(Result.ResultCode.SYSTEM_ERROR.getCode(), "Failed to send verification code, please try again later");
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            return false;
        }

        // Support multiple verification types (register, reset password, etc.)
        boolean verified = false;

        // Try to verify register type code
        String registerKey = EMAIL_CODE_PREFIX + "register:" + email;
        String storedRegisterCode = redisTemplate.opsForValue().get(registerKey);
        if (StringUtils.hasText(storedRegisterCode) && storedRegisterCode.equals(code)) {
            verified = true;
            // Delete code after successful verification
            redisTemplate.delete(registerKey);
        }

        // Try to verify reset password type code
        if (!verified) {
            String resetKey = EMAIL_CODE_PREFIX + "reset:" + email;
            String storedResetCode = redisTemplate.opsForValue().get(resetKey);
            if (StringUtils.hasText(storedResetCode) && storedResetCode.equals(code)) {
                verified = true;
                // Delete code after successful verification
                redisTemplate.delete(resetKey);
            }
        }

        return verified;
    }

    /**
     * Generate random verification code
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * Send email
     */
    private void sendEmail(String to, String code, String type) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(String.format("%s <%s>", emailNickname, fromEmail));
        helper.setTo(to);

        // Set different subject and content based on type
        String subject;
        String content;

        if ("reset".equals(type)) {
            subject = "Reset Password Verification Code - AI Assistant";
            content = "Hello,<br><br>You are resetting your AI Assistant account password. Your verification code is:<br><br>" +
                    "<h2 style='color:#335599;'>" + code + "</h2><br>" +
                    "This verification code is valid for 5 minutes. Do not share it with anyone.<br><br>" +
                    "If you did not request this, please ignore this email.<br><br>" +
                    "AI Assistant Team";
        } else {
            // Default is registration verification code
            subject = "Registration Verification Code - AI Assistant";
            content = "Hello,<br><br>Thank you for registering with AI Assistant. Your verification code is:<br><br>" +
                    "<h2 style='color:#335599;'>" + code + "</h2><br>" +
                    "This verification code is valid for 5 minutes. Do not share it with anyone.<br><br>" +
                    "AI Assistant Team";
        }

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
} 