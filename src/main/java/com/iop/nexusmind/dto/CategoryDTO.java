package com.iop.nexusmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分类数据传输对象")
public class CategoryDTO {
    
    @Schema(description = "分类 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "分类名称", example = "技术学习", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
    @Schema(description = "分类描述", example = "编程和技术相关笔记")
    private String description;
    
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
