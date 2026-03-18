package com.iop.nexusmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "错误响应信息")
public class ErrorResponse {
    
    @Schema(description = "错误发生时间", example = "2024-01-01T12:00:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP 状态码", example = "404")
    private Integer status;
    
    @Schema(description = "错误消息", example = "笔记未找到")
    private String message;
}
