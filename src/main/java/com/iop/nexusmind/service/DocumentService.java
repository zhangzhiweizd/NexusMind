package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Document;
import java.util.List;

public interface DocumentService {
    
    List<Document> getDocumentsByFolder(Long folderId);
    
    Document getDocumentById(Long id);
    
    Document createDocument(Document document);
    
    Document updateDocument(Long id, Document document);
    
    void deleteDocument(Long id);
    
    List<Document> searchDocuments(String keyword);
}
