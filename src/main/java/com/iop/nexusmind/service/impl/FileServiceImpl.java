package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.entity.FileAttachment;
import com.iop.nexusmind.entity.User;
import com.iop.nexusmind.exception.BusinessException;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.FileAttachmentRepository;
import com.iop.nexusmind.repository.UserRepository;
import com.iop.nexusmind.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileAttachmentRepository fileAttachmentRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.local-path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.max-file-size:10485760}")
    private long maxFileSize;

    @Value("${file.upload.url-prefix:/api/files}")
    private String urlPrefix;

    public FileServiceImpl(FileAttachmentRepository fileAttachmentRepository,
                          UserRepository userRepository) {
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * 上传单个文件
     */
    @Override
    public FileAttachment uploadFile(MultipartFile file, Long documentId) {
        // 验证文件
        validateFile(file);

        try {
            // 获取当前用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User uploader = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

            // 生成存储文件名
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String storedName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

            // 确定文件类型
            String fileType = determineFileType(file.getContentType());

            // 创建存储目录（按日期分目录）
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadDir = Paths.get(uploadPath, datePath);
            Files.createDirectories(uploadDir);

            // 保存文件
            Path filePath = uploadDir.resolve(storedName);
            Files.copy(file.getInputStream(), filePath);

            // 创建文件记录
            FileAttachment attachment = new FileAttachment();
            attachment.setOriginalName(originalFilename);
            attachment.setStoredName(storedName);
            attachment.setFilePath(datePath + "/" + storedName);
            attachment.setFileSize(file.getSize());
            attachment.setContentType(file.getContentType());
            attachment.setFileType(fileType);
            attachment.setExtension(extension);
            attachment.setUploader(uploader);
            attachment.setDocumentId(documentId);
            attachment.setAccessUrl(urlPrefix + "/" + attachment.getId() + "/access");
            attachment.setIsPublic(false);
            attachment.setDownloadCount(0);

            FileAttachment saved = fileAttachmentRepository.save(attachment);
            
            // 更新访问URL（需要ID）
            saved.setAccessUrl(urlPrefix + "/" + saved.getId() + "/access");
            return fileAttachmentRepository.save(saved);

        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量上传文件
     */
    @Override
    public List<FileAttachment> uploadFiles(MultipartFile[] files, Long documentId) {
        List<FileAttachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                attachments.add(uploadFile(file, documentId));
            }
        }
        return attachments;
    }

    /**
     * 根据ID获取文件信息
     */
    @Override
    public FileAttachment getFileById(Long id) {
        return fileAttachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文件不存在"));
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(Long id) {
        FileAttachment attachment = getFileById(id);
        
        try {
            // 删除物理文件
            Path filePath = Paths.get(uploadPath, attachment.getFilePath());
            Files.deleteIfExists(filePath);
            
            // 删除数据库记录
            fileAttachmentRepository.delete(attachment);
        } catch (IOException e) {
            throw new BusinessException("文件删除失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取文档的所有附件
     */
    @Override
    public List<FileAttachment> getFilesByDocument(Long documentId) {
        return fileAttachmentRepository.findByDocumentId(documentId);
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空", HttpStatus.BAD_REQUEST);
        }

        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小超过限制（最大" + (maxFileSize / 1024 / 1024) + "MB）", 
                    HttpStatus.BAD_REQUEST);
        }

        // 可以在这里添加文件类型白名单验证
        String contentType = file.getContentType();
        if (contentType == null || !isValidContentType(contentType)) {
            throw new BusinessException("不支持的文件类型", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 判断是否为有效的内容类型
     */
    private boolean isValidContentType(String contentType) {
        return contentType.startsWith("image/") || 
               contentType.startsWith("video/") || 
               contentType.equals("application/pdf") ||
               contentType.startsWith("text/");
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 确定文件类型
     */
    private String determineFileType(String contentType) {
        if (contentType == null) {
            return "OTHER";
        }
        if (contentType.startsWith("image/")) {
            return "IMAGE";
        } else if (contentType.startsWith("video/")) {
            return "VIDEO";
        } else if (contentType.equals("application/pdf")) {
            return "DOCUMENT";
        } else {
            return "OTHER";
        }
    }
}
