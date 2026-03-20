package com.iop.nexusmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "标签数据传输对象")
public class TagDTO {
    
    @Schema(description = "标签 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "标签名称", example = "Java", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
    @Schema(description = "标签颜色", example = "#FF6B6B")
    private String color;
    
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
