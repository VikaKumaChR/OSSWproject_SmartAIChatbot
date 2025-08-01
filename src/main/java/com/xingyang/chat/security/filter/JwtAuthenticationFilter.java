package com.xingyang.chat.security.filter;

import com.xingyang.chat.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT Authentication Filter
 *
 * @author XingYang
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    
    // List of paths that should skip token validation
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/captcha/**",
        "/auth/**",
        "/register",
        "/doc.html",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/webjars/**"
    );
    
    private final JwtTokenUtil jwtTokenUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        
        // Check if the request path matches any of the public paths
        boolean isPublicPath = PUBLIC_PATHS.stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
        
        if (isPublicPath) {
            log.debug("Skipping JWT authentication for public path: {}", requestPath);
        }
        
        return isPublicPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());
            String jwt = resolveToken(request);
            
            if (StringUtils.hasText(jwt)) {
                log.debug("JWT token found in request");
                
                if (jwtTokenUtil.validateToken(jwt)) {
                    log.debug("JWT token is valid");
                    
                    // Get authentication object from JWT token
                    Authentication authentication = jwtTokenUtil.getAuthentication(jwt);
                    
                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Set Authentication to security context for '{}', uri: {}", 
                            authentication.getName(), request.getRequestURI());
                    
                    // 尝试获取用户ID（可选，仅用于日志）
                    try {
                        Long userId = jwtTokenUtil.getUserIdFromToken("Bearer " + jwt);
                        log.debug("User ID from token: {}", userId);
                    } catch (Exception e) {
                        log.warn("Could not extract user ID from token", e);
                    }
                } else {
                    log.warn("Invalid JWT token");
                }
            } else {
                log.debug("No JWT token found in request");
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    log.debug("No authentication found in security context");
                } else {
                    log.debug("Authentication already exists in security context: {}", 
                            SecurityContextHolder.getContext().getAuthentication());
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
} 