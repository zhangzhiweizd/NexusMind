package com.iop.nexusmind.controller;

import com.iop.nexusmind.dto.NoteDTO;
import com.iop.nexusmind.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "笔记管理", description = "提供笔记的增删改查、搜索和 AI 摘要功能")
public class NoteController {

    private final NoteService noteService;

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
            @RequestBody NoteDTO noteDTO) {
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
            @RequestBody NoteDTO noteDTO) {
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
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<NoteDTO>> getAllNotes(
            @Parameter(description = "页码，从 0 开始")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(noteService.getAllNotes(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索笔记", description = "根据关键词搜索笔记（支持标题和内容模糊匹配）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "搜索成功",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<NoteDTO>> searchNotes(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword,
            @Parameter(description = "页码")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(noteService.searchNotes(keyword, pageable));
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
}
