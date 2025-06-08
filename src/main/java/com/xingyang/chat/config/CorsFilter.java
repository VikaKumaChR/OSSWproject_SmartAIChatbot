package com.xingyang.chat.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局CORS过滤器
 * 确保所有请求都能正确处理跨域问题
 *
 * @author XingYang
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        // 设置允许的源
        response.setHeader("Access-Control-Allow-Origin", 
                request.getHeader("Origin") != null 
                        && (request.getHeader("Origin").contains("localhost:3000") 
                        || request.getHeader("Origin").contains("localhost:3001")) 
                ? request.getHeader("Origin") : "http://localhost:3000");
        
        // 设置允许的HTTP方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        // 设置预检请求的缓存时间
        response.setHeader("Access-Control-Max-Age", "3600");
        // 设置允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
        // 设置是否允许发送Cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // 对于OPTIONS请求直接返回成功
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
} 