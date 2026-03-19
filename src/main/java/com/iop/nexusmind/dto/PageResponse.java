package com.iop.nexusmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分页响应数据")
public class PageResponse<T> {

    @Schema(description = "当前页数据列表", example = "[...]")
    private List<T> content;

    @Schema(description = "当前页码（从 0 开始）", example = "0")
    private int pageNumber;

    @Schema(description = "每页大小", example = "10")
    private int pageSize;

    @Schema(description = "总记录数", example = "100")
    private long totalElements;

    @Schema(description = "总页数", example = "10")
    private int totalPages;

    @Schema(description = "是否第一页", example = "true")
    private boolean first;

    @Schema(description = "是否最后一页", example = "false")
    private boolean last;

    @Schema(description = "是否为空", example = "false")
    private boolean empty;

    @Schema(hidden = true)
    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}
