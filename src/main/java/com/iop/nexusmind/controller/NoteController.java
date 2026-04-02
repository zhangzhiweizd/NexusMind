package com.iop.nexusmind.controller;

import com.iop.nexusmind.dto.NoteDTO;
import com.iop.nexusmind.dto.PageResponse;
import com.iop.nexusmind.service.ExportService;
import com.iop.nexusmind.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "笔记管理", description = "提供笔记的增删改查、搜索和 AI 摘要功能")
public class NoteController {

    private final NoteService noteService;
    private final ExportService exportService;

    @PostMapping
    @Operation(summary = "创建笔记", description = "创建一个新的笔记，支持标题、内容、分类和标签")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功",
                    content = @Content(schema = @Schema(implementation = NoteDTO.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<NoteDTO> createNote(
            @Parameter(description = "笔记信息", required = true)
            @RequestBody @Valid NoteDTO noteDTO) {
        NoteDTO created = noteService.createNote(noteDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新笔记", description = "根据 ID 更新指定笔记的信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = NoteDTO.class))),
            @ApiResponse(responseCode = "404", description = "笔记未找到"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    public ResponseEntity<NoteDTO> updateNote(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "更新后的笔记信息", required = true)
            @RequestBody @Valid NoteDTO noteDTO) {
        NoteDTO updated = noteService.updateNote(id, noteDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取笔记详情", description = "根据 ID 获取单个笔记的完整信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = NoteDTO.class))),
            @ApiResponse(responseCode = "404", description = "笔记未找到")
    })
    public ResponseEntity<NoteDTO> getNote(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除笔记", description = "根据 ID 删除指定笔记")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "删除成功"),
            @ApiResponse(responseCode = "404", description = "笔记未找到")
    })
    public ResponseEntity<Void> deleteNote(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "获取所有笔记（分页）", description = "分页获取所有笔记列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    public ResponseEntity<PageResponse<NoteDTO>> getAllNotes(
            @Parameter(description = "页码，从 0 开始")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NoteDTO> pageResult = noteService.getAllNotes(pageable);
        PageResponse<NoteDTO> response = PageResponse.fromPage(pageResult);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索笔记", description = "根据关键词搜索笔记（支持标题和内容模糊匹配）")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "搜索成功",
            content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    public ResponseEntity<PageResponse<NoteDTO>> searchNotes(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword,
            @Parameter(description = "页码")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NoteDTO> pageResult = noteService.searchNotes(keyword, pageable);
        PageResponse<NoteDTO> response = PageResponse.fromPage(pageResult);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "按分类获取笔记", description = "获取指定分类下的所有笔记")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "分类不存在")
    })
    public ResponseEntity<List<NoteDTO>> getNotesByCategory(
            @Parameter(description = "分类 ID", required = true)
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(noteService.getNotesByCategory(categoryId));
    }

    @GetMapping("/tag/{tagId}")
    @Operation(summary = "按标签获取笔记", description = "获取包含指定标签的所有笔记")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "标签不存在")
    })
    public ResponseEntity<List<NoteDTO>> getNotesByTag(
            @Parameter(description = "标签 ID", required = true)
            @PathVariable Long tagId) {
        return ResponseEntity.ok(noteService.getNotesByTag(tagId));
    }

    @PostMapping("/{id}/summary")
    @Operation(summary = "生成 AI 摘要", description = "调用 AI 模型为指定笔记生成摘要")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "生成成功",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "笔记未找到"),
            @ApiResponse(responseCode = "500", description = "AI 服务调用失败")
    })
    public ResponseEntity<Map<String, String>> generateSummary(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id) {
        NoteDTO note = noteService.getNoteById(id);
        String summary = noteService.generateSummary(note.getContent());

        Map<String, String> response = new HashMap<>();
        response.put("summary", summary);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/export/markdown")
    @Operation(summary = "导出笔记为 Markdown", description = "将指定笔记导出为 Markdown 格式")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "导出成功",
                    content = @Content(mediaType = "text/markdown")),
            @ApiResponse(responseCode = "404", description = "笔记未找到")
    })
    public ResponseEntity<String> exportNoteToMarkdown(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id) {
        String markdown = exportService.exportToMarkdown(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/markdown; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + 
                        java.net.URLEncoder.encode(markdown, StandardCharsets.UTF_8) + ".md\"")
                .body(markdown);
    }

    @GetMapping("/{id}/export/word")
    @Operation(summary = "导出笔记为 Word", description = "将指定笔记导出为 Word 文档")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "导出成功",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")),
            @ApiResponse(responseCode = "404", description = "笔记未找到")
    })
    public ResponseEntity<byte[]> exportNoteToWord(
            @Parameter(description = "笔记 ID", required = true)
            @PathVariable Long id) throws IOException {
        NoteDTO note = noteService.getNoteById(id);
        log.info("导出 Word 文档，笔记 ID: {}, 标题：{}, 内容长度：{}", id, note.getTitle(), note.getContent() != null ? note.getContent().length() : 0);
        
        // 检查内容中是否有图片
        if (note.getContent() != null && note.getContent().contains("<img")) {
            log.info("笔记包含图片");
        }
        
        byte[] wordContent = exportService.exportToWord(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = java.net.URLEncoder.encode(note.getTitle(), "UTF-8") + ".docx";
        headers.setContentDispositionFormData("attachment", fileName);
        
        log.info("Word 文档生成完成，大小：{} bytes", wordContent.length);
        return new ResponseEntity<>(wordContent, headers, HttpStatus.OK);
    }

    @PostMapping("/export/markdown")
    @Operation(summary = "批量导出笔记为 Markdown(ZIP)", description = "将多个笔记导出为 Markdown 并打包成 ZIP 文件")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "导出成功",
                    content = @Content(mediaType = "application/zip"))
    })
    public ResponseEntity<byte[]> exportMultipleToMarkdown(
            @Parameter(description = "笔记 ID 列表", required = true)
            @RequestBody List<Long> noteIds) throws IOException {
        byte[] zipContent = exportService.exportMultipleToMarkdown(noteIds);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "notes_export.zip");
        
        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
    }

    @PostMapping("/export/word")
    @Operation(summary = "批量导出笔记为 Word(ZIP)", description = "将多个笔记导出为 Word 文档并打包成 ZIP 文件")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "导出成功",
                    content = @Content(mediaType = "application/zip"))
    })
    public ResponseEntity<byte[]> exportMultipleToWord(
            @Parameter(description = "笔记 ID 列表", required = true)
            @RequestBody List<Long> noteIds) throws IOException {
        byte[] zipContent = exportService.exportMultipleToWord(noteIds);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "notes_export_word.zip");
        
        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
    }
}
