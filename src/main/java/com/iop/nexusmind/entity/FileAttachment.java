package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 文件实体类
 * 用于管理上传的图片、视频、文档等附件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "file_attachment")
@EntityListeners(AuditingEntityListener.class)
public class FileAttachment extends BaseEntity {

    /** 原始文件名 */
    @Column(nullable = false, length = 255)
    private String originalName;

    /** 存储文件名（UUID或其他唯一标识） */
    @Column(nullable = false, length = 255)
    private String storedName;

    /** 文件路径（相对路径或URL） */
    @Column(nullable = false, length = 500)
    private String filePath;

    /** 文件大小（字节） */
    @Column(nullable = false)
    private Long fileSize;

    /** 文件MIME类型 */
    @Column(nullable = false, length = 100)
    private String contentType;

    /** 文件类型：IMAGE, VIDEO, DOCUMENT, OTHER */
    @Column(nullable = false, length = 20)
    private String fileType;

    /** 文件扩展名 */
    @Column(length = 10)
    private String extension;

    /** 上传者 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    /** 关联的文档ID（可选） */
    @Column(name = "document_id")
    private Long documentId;

    /** 访问URL */
    @Column(length = 500)
    private String accessUrl;

    /** 是否公开访问 */
    @Column(nullable = false)
    private Boolean isPublic = false;

    /** 下载次数 */
    @Column(nullable = false)
    private Integer downloadCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
