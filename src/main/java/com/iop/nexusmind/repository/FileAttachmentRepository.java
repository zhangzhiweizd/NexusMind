package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件附件Repository
 */
@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
    
    /**
     * 根据文档ID查询所有附件
     */
    List<FileAttachment> findByDocumentId(Long documentId);
    
    /**
     * 根据上传者查询文件
     */
    List<FileAttachment> findByUploaderId(Long uploaderId);
    
    /**
     * 根据存储文件名查询
     */
    FileAttachment findByStoredName(String storedName);
}
