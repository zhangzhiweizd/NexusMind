package com.iop.nexusmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "笔记数据传输对象")
public class NoteDTO {

    @Schema(description = "笔记 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "笔记标题", example = "我的第一篇笔记", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "笔记内容", example = "这是笔记的正文内容...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "AI 生成的摘要", example = "这是一篇关于...的笔记", accessMode = Schema.AccessMode.READ_ONLY)
    private String summary;

    @Schema(description = "分类 ID", example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "技术学习", accessMode = Schema.AccessMode.READ_ONLY)
    private String categoryName;

    @Schema(description = "标签 ID 列表", example = "[1, 2, 3]")
    private Set<Long> tagIds;

    @Schema(description = "是否公开", example = "false", defaultValue = "false")
    private Boolean isPublic;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
