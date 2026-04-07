package com.iop.nexusmind.controller;

import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@Tag(name = "工作空间管理", description = "工作空间相关接口")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * 构造函数，注入WorkspaceService
     */
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    @Operation(summary = "获取所有工作空间")
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 获取工作空间")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    @PostMapping
    @Operation(summary = "创建工作空间")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        return ResponseEntity.ok(workspaceService.createWorkspace(workspace));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新工作空间")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable Long id, 
                                                     @RequestBody Workspace workspace) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(id, workspace));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除工作空间")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok().build();
    }
}
