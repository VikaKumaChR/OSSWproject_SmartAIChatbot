package com.xingyang.chat.controller;

import com.xingyang.chat.model.dto.LoginDTO;
import com.xingyang.chat.model.dto.RegisterDTO;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.model.vo.UserVO;
import com.xingyang.chat.service.CaptchaService;
import com.xingyang.chat.service.EmailService;
import com.xingyang.chat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Authentication Controller
 *
 * @author XingYang
 */
@Slf4j
@Tag(name = "Authentication API", description = "Authentication endpoints for login, registration, and user information")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private EmailService emailService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "User Login",
        description = "Login with username, password and captcha to get JWT token"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = Result.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = Result.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Parameter(description = "Login credentials", required = true) @Validated @RequestBody LoginDTO loginDTO) {
        log.info("User login attempt: {}", loginDTO.getUsername());
        log.info("Login request captcha details - UUID: {}, Captcha: {}", loginDTO.getCaptchaUuid(), loginDTO.getCaptcha());
        
        // Validate captcha
        boolean isValidCaptcha = captchaService.validateCaptcha(loginDTO.getCaptchaUuid(), loginDTO.getCaptcha());
        log.info("Captcha validation result for user {}: {}", loginDTO.getUsername(), isValidCaptcha);
        
        if (!isValidCaptcha) {
            log.warn("Login failed for user {} due to invalid captcha", loginDTO.getUsername());
            return Result.error(Result.ResultCode.PARAM_ERROR.getCode(), "Invalid captcha");
        }
        
        log.info("Captcha validation successful, proceeding with authentication for user: {}", loginDTO.getUsername());
        Map<String, Object> result = userService.login(loginDTO);
        log.info("Login successful for user: {}", loginDTO.getUsername());
        return Result.success("Login successful", result);
    }

    @Operation(
        summary = "User Register",
        description = "Register a new account with username, password, email and verification code"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(schema = @Schema(implementation = Result.class))),
        @ApiResponse(responseCode = "400", description = "Invalid registration data", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @PostMapping("/register")
    public Result<UserVO> register(@Parameter(description = "Registration data", required = true) @Validated @RequestBody RegisterDTO registerDTO) {
        log.info("User registration: {}", registerDTO.getUsername());
        
        // Validate captcha
        boolean isValidCaptcha = captchaService.validateCaptcha(registerDTO.getCaptchaUuid(), registerDTO.getCaptcha());
        if (!isValidCaptcha) {
            return Result.error(Result.ResultCode.PARAM_ERROR.getCode(), "Invalid captcha");
        }
        
        UserVO user = userService.register(registerDTO);
        return Result.success("Registration successful", user);
    }

    @Operation(
        summary = "Current User",
        description = "Get information about the current authenticated user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User information retrieved successfully", content = @Content(schema = @Schema(implementation = Result.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Result.class)))
    })
    @GetMapping("/user")
    public Result<UserVO> getCurrentUser() {
        log.info("Get current user information");
        UserVO user = userService.getCurrentUser();
        return Result.success(user);
    }
} 