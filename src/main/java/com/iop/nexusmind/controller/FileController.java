package com.iop.nexusmind.controller;

import com.iop.nexusmind.entity.FileAttachment;
import com.iop.nexusmind.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件管理Controller
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "文件管理", description = "文件上传、下载和管理接口")
public class FileController {

    private final FileService fileService;
    private final String uploadPath;

    public FileController(FileService fileService,
                         @org.springframework.beans.factory.annotation.Value("${file.upload.local-path:./uploads}") 
                         String uploadPath) {
        this.fileService = fileService;
        this.uploadPath = uploadPath;
    }

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传图片、视频等文件")
    public ResponseEntity<FileAttachment> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "documentId", required = false) Long documentId) {
        FileAttachment attachment = fileService.uploadFile(file, documentId);
        return ResponseEntity.ok(attachment);
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/upload/batch")
    @Operation(summary = "批量上传文件", description = "一次上传多个文件")
    public ResponseEntity<List<FileAttachment>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "documentId", required = false) Long documentId) {
        List<FileAttachment> attachments = fileService.uploadFiles(files, documentId);
        return ResponseEntity.ok(attachments);
    }

    /**
     * 获取文件信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取文件信息", description = "根据ID获取文件元数据")
    public ResponseEntity<FileAttachment> getFile(@PathVariable Long id) {
        FileAttachment attachment = fileService.getFileById(id);
        return ResponseEntity.ok(attachment);
    }

    /**
     * 访问/下载文件
     */
    @GetMapping("/{id}/access")
    @Operation(summary = "访问文件", description = "获取文件内容，支持图片和视频")
    public ResponseEntity<Resource> accessFile(@PathVariable Long id) {
        try {
            FileAttachment attachment = fileService.getFileById(id);
            
            Path filePath = Paths.get(uploadPath).resolve(attachment.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 增加下载次数
            attachment.setDownloadCount(attachment.getDownloadCount() + 1);
            // 注意：这里需要调用service更新，简化起见省略

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + attachment.getOriginalName() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "删除指定的文件")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取文档的所有附件
     */
    @GetMapping("/document/{documentId}")
    @Operation(summary = "获取文档附件", description = "获取指定文档的所有附件列表")
    public ResponseEntity<List<FileAttachment>> getFilesByDocument(@PathVariable Long documentId) {
        List<FileAttachment> attachments = fileService.getFilesByDocument(documentId);
        return ResponseEntity.ok(attachments);
    }
}
