package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Document;
import java.util.List;

public interface DocumentService {
    
    /**
     * 获取指定文件夹下的所有文档
     * @param folderId 文件夹ID
     * @return 文档列表
     */
    List<Document> getDocumentsByFolder(Long folderId);
    
    /**
     * 根据ID获取文档详情
     * @param id 文档ID
     * @return 文档对象
     */
    Document getDocumentById(Long id);
    
    /**
     * 创建新文档
     * @param document 文档对象
     * @return 创建后的文档对象
     */
    Document createDocument(Document document);
    
    /**
     * 更新文档信息
     * @param id 文档ID
     * @param document 更新的文档信息
     * @return 更新后的文档对象
     */
    Document updateDocument(Long id, Document document);
    
    /**
     * 删除文档
     * @param id 文档ID
     */
    void deleteDocument(Long id);
    
    /**
     * 搜索文档（按标题模糊搜索）
     * @param keyword 搜索关键词
     * @return 匹配的文档列表
     */
    List<Document> searchDocuments(String keyword);
}
