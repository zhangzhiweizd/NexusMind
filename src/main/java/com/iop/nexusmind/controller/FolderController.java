package com.iop.nexusmind.controller;

import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@Tag(name = "文件夹管理", description = "文件夹相关接口")
public class FolderController {

    private final FolderService folderService;

    /**
     * 构造函数，注入FolderService
     */
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/workspace/{workspaceId}")
    @Operation(summary = "获取工作空间下的所有文件夹")
    public ResponseEntity<List<Folder>> getFoldersByWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(folderService.getFoldersByWorkspace(workspaceId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 获取文件夹")
    public ResponseEntity<Folder> getFolderById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.getFolderById(id));
    }

    @PostMapping
    @Operation(summary = "创建文件夹")
    public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {
        return ResponseEntity.ok(folderService.createFolder(folder));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文件夹")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, 
                                               @RequestBody Folder folder) {
        return ResponseEntity.ok(folderService.updateFolder(id, folder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件夹")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        return ResponseEntity.ok().build();
    }
}
