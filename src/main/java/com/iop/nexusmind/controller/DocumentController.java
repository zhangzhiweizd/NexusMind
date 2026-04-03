package com.iop.nexusmind.controller;

import com.iop.nexusmind.entity.Document;
import com.iop.nexusmind.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "文档管理", description = "文档相关接口")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/folder/{folderId}")
    @Operation(summary = "获取文件夹下的所有文档")
    public ResponseEntity<List<Document>> getDocumentsByFolder(@PathVariable Long folderId) {
        return ResponseEntity.ok(documentService.getDocumentsByFolder(folderId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 获取文档")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping
    @Operation(summary = "创建文档")
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        return ResponseEntity.ok(documentService.createDocument(document));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文档")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, 
                                                   @RequestBody Document document) {
        return ResponseEntity.ok(documentService.updateDocument(id, document));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文档")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "搜索文档")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam String keyword) {
        return ResponseEntity.ok(documentService.searchDocuments(keyword));
    }
}
