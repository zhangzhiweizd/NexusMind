package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.FileAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传单个文件
     * @param file 上传的文件
     * @param documentId 关联的文档ID（可选）
     * @return 文件附件信息
     */
    FileAttachment uploadFile(MultipartFile file, Long documentId);
    
    /**
     * 批量上传文件
     * @param files 上传的文件列表
     * @param documentId 关联的文档ID（可选）
     * @return 文件附件列表
     */
    List<FileAttachment> uploadFiles(MultipartFile[] files, Long documentId);
    
    /**
     * 根据ID获取文件信息
     * @param id 文件ID
     * @return 文件附件信息
     */
    FileAttachment getFileById(Long id);
    
    /**
     * 删除文件
     * @param id 文件ID
     */
    void deleteFile(Long id);
    
    /**
     * 获取文档的所有附件
     * @param documentId 文档ID
     * @return 文件附件列表
     */
    List<FileAttachment> getFilesByDocument(Long documentId);
}
