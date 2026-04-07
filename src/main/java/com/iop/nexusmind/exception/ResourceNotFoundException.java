package com.iop.nexusmind.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源未找到异常
 * 用于处理请求的资源不存在的情况
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
