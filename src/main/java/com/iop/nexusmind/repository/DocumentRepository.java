package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Document;
import com.iop.nexusmind.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文档数据访问接口
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    /**
     * 根据文件夹查找文档列表
     */
    List<Document> findByFolder(Folder folder);
    
    /**
     * 根据创建者ID查找文档列表
     */
    List<Document> findByCreatorId(Long creatorId);
    
    /**
     * 根据标题模糊搜索文档（忽略大小写）
     */
    List<Document> findByTitleContainingIgnoreCase(String title);
}
