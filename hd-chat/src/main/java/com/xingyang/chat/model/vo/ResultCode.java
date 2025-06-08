package com.xingyang.chat.model.vo;

/**
 * Standard API result codes
 * 
 * @author xingyang
 */
public enum ResultCode {
    
    /**
     * Success
     */
    SUCCESS(200, "Success"),
    
    /**
     * General error
     */
    ERROR(500, "Error"),
    
    /**
     * Parameter error
     */
    PARAM_ERROR(400, "Parameter error"),
    
    /**
     * Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized"),
    
    /**
     * Forbidden
     */
    FORBIDDEN(403, "Forbidden"),
    
    /**
     * Resource not found
     */
    NOT_FOUND(404, "Resource not found"),
    
    /**
     * Method not allowed
     */
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    
    /**
     * Server internal error
     */
    INTERNAL_SERVER_ERROR(500, "Server internal error");
    
    private final int code;
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
} 