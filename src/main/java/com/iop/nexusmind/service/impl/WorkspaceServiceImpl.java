package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.entity.User;
import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.repository.UserRepository;
import com.iop.nexusmind.repository.WorkspaceRepository;
import com.iop.nexusmind.service.WorkspaceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
                               UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        
        // 返回用户创建或参与的工作空间
        return workspaceRepository.findByCreator(currentUser);
    }

    @Override
    public Workspace getWorkspaceById(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("工作空间不存在"));
    }

    @Override
    public Workspace createWorkspace(Workspace workspace) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByUsername(username).orElseThrow();
        
        workspace.setCreator(creator);
        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace updateWorkspace(Long id, Workspace workspaceDetails) {
        Workspace workspace = getWorkspaceById(id);
        
        workspace.setName(workspaceDetails.getName());
        workspace.setDescription(workspaceDetails.getDescription());
        
        return workspaceRepository.save(workspace);
    }

    @Override
    public void deleteWorkspace(Long id) {
        Workspace workspace = getWorkspaceById(id);
        workspaceRepository.delete(workspace);
    }
}
