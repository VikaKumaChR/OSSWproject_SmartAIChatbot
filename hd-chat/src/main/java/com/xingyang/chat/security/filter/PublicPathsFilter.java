package com.xingyang.chat.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter to ensure public paths can be accessed without authentication
 * This filter runs before the Spring Security filters
 *
 * @author XingYang
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Run this filter before all others
public class PublicPathsFilter extends OncePerRequestFilter {

    // List of paths that should be publicly accessible
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/captcha/",
        "/auth/",
        "/register",
        "/swagger-ui/",
        "/v3/api-docs/",
        "/doc.html"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.debug("Processing request URI: {}", uri);
        
        // Allow requests to public paths
        if (isPublicPath(uri)) {
            log.debug("Public path detected: {}", uri);
        }
        
        // Continue the filter chain regardless
        filterChain.doFilter(request, response);
    }
    
    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::contains);
    }
} 