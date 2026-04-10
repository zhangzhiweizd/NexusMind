package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
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
@Comment("文档表")
@EntityListeners(AuditingEntityListener.class)
public class Document extends BaseEntity {

    @Column(nullable = false, length = 500)
    @Comment("文档标题")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Comment("文档内容（JSONB格式，存储富文本内容）")
    private String content;

    @Column(columnDefinition = "TEXT")
    @Comment("纯文本内容，用于搜索")
    private String plainText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    @Comment("所属文件夹ID")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    @Comment("创建者ID")
    private User creator;

    @Column(length = 50)
    @Comment("状态：DRAFT-草稿, PUBLISHED-已发布, ARCHIVED-已归档")
    private String status = "DRAFT";

    @Column(length = 500)
    @Comment("标签")
    private String tags;

    @Version
    @Comment("版本号，用于乐观锁")
    private Long version = 0L;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
