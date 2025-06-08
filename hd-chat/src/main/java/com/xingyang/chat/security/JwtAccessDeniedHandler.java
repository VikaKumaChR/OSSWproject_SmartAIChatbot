package com.xingyang.chat.security;

import com.alibaba.fastjson.JSON;
import com.xingyang.chat.model.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handle access denied exceptions
 *
 * @author XingYang
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        Result<?> result = Result.error(Result.ResultCode.FORBIDDEN.getCode(), 
                                      accessDeniedException.getMessage() != null ? 
                                      accessDeniedException.getMessage() : "Access denied");
        
        response.getWriter().write(JSON.toJSONString(result));
    }
} 