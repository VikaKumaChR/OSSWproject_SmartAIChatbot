package com.xingyang.chat.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * MyBatis Plus Configuration
 *
 * @author XingYang
 */
@Slf4j
@Configuration
public class MyBatisPlusConfig implements MetaObjectHandler {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // Add pagination plugin
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // Add optimistic locking plugin
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * Automatically fill create fields on insert
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        
        // Set creator ID from Security Context
        Long userId = getCurrentUserId();
        if (userId != null) {
            this.strictInsertFill(metaObject, "createBy", () -> userId, Long.class);
            this.strictInsertFill(metaObject, "updateBy", () -> userId, Long.class);
        }
        
        // Set default values for other fields
        this.strictInsertFill(metaObject, "deleted", () -> 0, Integer.class);
        this.strictInsertFill(metaObject, "version", () -> 1, Integer.class);
    }

    /**
     * Automatically fill update fields on update
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        
        // Set updater ID from Security Context
        Long userId = getCurrentUserId();
        if (userId != null) {
            this.strictUpdateFill(metaObject, "updateBy", () -> userId, Long.class);
        }
    }
    
    /**
     * Get current user ID from Security Context
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof Long) {
                    return (Long) principal;
                }
            }
            return null;
        } catch (Exception e) {
            log.warn("Failed to get current user ID: {}", e.getMessage());
            return null;
        }
    }
} 