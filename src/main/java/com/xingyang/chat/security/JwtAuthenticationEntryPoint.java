package com.xingyang.chat.security;

import com.alibaba.fastjson.JSON;
import com.xingyang.chat.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Handle authentication exceptions for unauthorized requests
 *
 * @author XingYang
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // List of paths that should be publicly accessible
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/captcha/**",
        "/auth/login", 
        "/auth/register"
    );
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        
        // Get the request URI
        String uri = request.getRequestURI();
        log.debug("Authentication entry point triggered for URI: {}", uri);
        
        // Check if this is a public path that shouldn't require authentication
        // but we will just log it as we will let Spring Security handle the permit-all logic
        if (isPublicPath(uri)) {
            log.debug("URI {} matches public path pattern - this should be allowed by Spring Security", uri);
        }
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        // Handle different authentication exceptions
        String errorMessage;
        if (authException instanceof BadCredentialsException) {
            // Just log the error type without stack trace for bad credentials
            log.info("Authentication failed: Invalid username or password");
            errorMessage = "Invalid username or password";
        } else {
            // Log other authentication errors with more detail
            log.info("Authentication failed: {}", authException.getMessage());
            errorMessage = authException.getMessage() != null ? 
                           authException.getMessage() : "Unauthorized access";
        }
        
        Result<?> result = Result.error(Result.ResultCode.UNAUTHORIZED.getCode(), errorMessage);
        response.getWriter().write(JSON.toJSONString(result));
    }
    
    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
} 