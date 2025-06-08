package com.xingyang.chat.controller;

import com.xingyang.chat.model.dto.EmailSendDTO;
import com.xingyang.chat.model.dto.EmailVerifyDTO;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Email Controller
 *
 * @author XingYang
 */
@RestController
@RequestMapping("/auth/email")
@Tag(name = "Email API", description = "Email verification related endpoints")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/code")
    @Operation(summary = "Send verification code to email")
    public Result<Void> sendVerificationCode(@RequestBody @Validated EmailSendDTO emailSendDTO) {
        log.info("Sending verification code to email: {}", emailSendDTO.getEmail());
        
        boolean success = emailService.sendCode(emailSendDTO.getEmail(), emailSendDTO.getType());
        
        if (success) {
            return Result.ok("Verification code sent successfully");
        } else {
            return Result.fail("Failed to send verification code");
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify email verification code")
    public Result<Void> verifyCode(@RequestBody @Validated EmailVerifyDTO emailVerifyDTO) {
        log.info("Verifying email code for: {}", emailVerifyDTO.getEmail());
        
        boolean verified = emailService.verifyCode(emailVerifyDTO.getEmail(), emailVerifyDTO.getCode());
        
        if (verified) {
            return Result.ok("Verification successful");
        } else {
            return Result.fail("Invalid or expired verification code");
        }
    }
} 