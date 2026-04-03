package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Document;
import com.iop.nexusmind.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByFolder(Folder folder);
    
    List<Document> findByCreatorId(Long creatorId);
    
    List<Document> findByTitleContainingIgnoreCase(String title);
}
