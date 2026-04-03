package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Folder;
import java.util.List;

public interface FolderService {
    
    List<Folder> getFoldersByWorkspace(Long workspaceId);
    
    Folder getFolderById(Long id);
    
    Folder createFolder(Folder folder);
    
    Folder updateFolder(Long id, Folder folder);
    
    void deleteFolder(Long id);
}
