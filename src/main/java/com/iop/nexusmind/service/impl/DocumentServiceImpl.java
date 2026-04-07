package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.entity.Document;
import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.entity.User;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.DocumentRepository;
import com.iop.nexusmind.repository.FolderRepository;
import com.iop.nexusmind.repository.UserRepository;
import com.iop.nexusmind.service.DocumentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    /**
     * 构造函数，注入依赖
     */
    public DocumentServiceImpl(DocumentRepository documentRepository,
                              FolderRepository folderRepository,
                              UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    /**
     * 获取指定文件夹下的所有文档
     */
    @Override
    public List<Document> getDocumentsByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("文件夹不存在"));
        return documentRepository.findByFolder(folder);
    }

    /**
     * 根据ID获取文档详情
     */
    @Override
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文档不存在"));
    }

    /**
     * 创建新文档
     * 自动设置创建者和纯文本内容
     */
    @Override
    public Document createDocument(Document document) {
        if (document.getFolder() != null && document.getFolder().getId() != null) {
            Folder folder = folderRepository.findById(document.getFolder().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("文件夹不存在"));
            document.setFolder(folder);
        }
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByUsername(username).orElseThrow();
        document.setCreator(creator);
        
        // 如果没有设置纯文本，从内容中提取
        if (document.getPlainText() == null && document.getContent() != null) {
            document.setPlainText(extractPlainText(document.getContent()));
        }
        
        return documentRepository.save(document);
    }

    /**
     * 更新文档信息
     * 支持更新标题、内容、纯文本、状态和标签
     */
    @Override
    public Document updateDocument(Long id, Document documentDetails) {
        Document document = getDocumentById(id);
        
        document.setTitle(documentDetails.getTitle());
        document.setContent(documentDetails.getContent());
        
        if (documentDetails.getPlainText() != null) {
            document.setPlainText(documentDetails.getPlainText());
        } else if (documentDetails.getContent() != null) {
            document.setPlainText(extractPlainText(documentDetails.getContent()));
        }
        
        document.setStatus(documentDetails.getStatus());
        document.setTags(documentDetails.getTags());
        
        return documentRepository.save(document);
    }

    /**
     * 删除文档
     */
    @Override
    public void deleteDocument(Long id) {
        Document document = getDocumentById(id);
        documentRepository.delete(document);
    }

    /**
     * 搜索文档（按标题模糊搜索）
     */
    @Override
    public List<Document> searchDocuments(String keyword) {
        return documentRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * 从 HTML 内容中提取纯文本
     */
    private String extractPlainText(String htmlContent) {
        // 简单的 HTML 标签移除，实际项目中可以使用 Jsoup
        return htmlContent.replaceAll("<[^>]*>", "");
    }
}
