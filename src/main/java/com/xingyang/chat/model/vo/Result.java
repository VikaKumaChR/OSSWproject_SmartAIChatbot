package com.xingyang.chat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Standard API Response Structure
 * 
 * @author XingYang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Status code
     */
    private Integer code;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data
     */
    private T data;
    
    /**
     * Timestamp
     */
    private long timestamp;
    
    /**
     * Success response with data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, System.currentTimeMillis());
    }
    
    /**
     * Success response with message and data
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, System.currentTimeMillis());
    }
    
    /**
     * Success response with message only
     */
    public static <T> Result<T> ok(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * Error response with message
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ResultCode.ERROR.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * Error response with code and message
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }
    
    /**
     * Error response with ResultCode enum
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null, System.currentTimeMillis());
    }
    
    /**
     * Error response with message
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.ERROR.getCode(), message, null, System.currentTimeMillis());
    }
    
    /**
     * Response code enum
     */
    public enum ResultCode {
        SUCCESS(200, "Success"),
        ERROR(500, "Server Error"),
        SYSTEM_ERROR(500, "System Error"),
        PARAM_ERROR(400, "Parameter Error"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Resource Not Found");
        
        private final Integer code;
        private final String message;
        
        ResultCode(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getMessage() {
            return message;
        }
    }
} 