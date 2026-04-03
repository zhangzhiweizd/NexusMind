package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 文档实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "document")
@EntityListeners(AuditingEntityListener.class)
public class Document extends BaseEntity {

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // JSONB 格式，存储富文本内容

    @Column(columnDefinition = "TEXT")
    private String plainText; // 纯文本内容，用于搜索

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(length = 50)
    private String status = "DRAFT"; // DRAFT, PUBLISHED, ARCHIVED

    @Column(length = 500)
    private String tags;

    @Version
    private Long version = 0L; // 版本号，用于乐观锁

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
