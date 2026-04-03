package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    
    List<Folder> findByWorkspace(Workspace workspace);
    
    List<Folder> findByParent(Folder parent);
    
    List<Folder> findByWorkspaceAndParentIsNull(Workspace workspace);
}
