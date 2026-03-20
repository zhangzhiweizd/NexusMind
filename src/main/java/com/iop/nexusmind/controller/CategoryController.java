package com.iop.nexusmind.controller;

import com.iop.nexusmind.dto.CategoryDTO;
import com.iop.nexusmind.dto.TagDTO;
import com.iop.nexusmind.service.CategoryService;
import com.iop.nexusmind.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    private final TagService tagService;

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类", description = "获取系统中所有的分类列表")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", 
        description = "获取成功",
        content = @io.swagger.v3.oas.annotations.media.Content(
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CategoryDTO.class)
        )
    )
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/tags")
    @Operation(summary = "获取所有标签", description = "获取系统中所有的标签列表")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200", 
        description = "获取成功",
        content = @io.swagger.v3.oas.annotations.media.Content(
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TagDTO.class)
        )
    )
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.findAll());
    }
}
