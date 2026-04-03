package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Workspace;
import java.util.List;

public interface WorkspaceService {
    
    List<Workspace> getAllWorkspaces();
    
    Workspace getWorkspaceById(Long id);
    
    Workspace createWorkspace(Workspace workspace);
    
    Workspace updateWorkspace(Long id, Workspace workspace);
    
    void deleteWorkspace(Long id);
}
