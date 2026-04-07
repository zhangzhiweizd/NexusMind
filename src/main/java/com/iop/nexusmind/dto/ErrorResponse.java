package com.iop.nexusmind.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一错误响应DTO
 */
@Data
public class ErrorResponse {
    
    /** 时间戳 */
    private LocalDateTime timestamp;
    
    /** HTTP状态码 */
    private int status;
    
    /** 错误信息 */
    private String error;
    
    /** 错误描述 */
    private String message;
    
    /** 请求路径 */
    private String path;
    
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
