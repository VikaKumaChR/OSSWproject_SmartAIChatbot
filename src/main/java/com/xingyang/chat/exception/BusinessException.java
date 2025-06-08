package com.xingyang.chat.exception;

import com.xingyang.chat.model.vo.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Custom Business Exception
 *
 * @author XingYang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Error code
     */
    private final Integer code;

    /**
     * Error message
     */
    private final String message;

    public BusinessException(Result.ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.code = Result.ResultCode.ERROR.getCode();
        this.message = message;
    }
} 