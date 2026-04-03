package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.repository.FolderRepository;
import com.iop.nexusmind.repository.WorkspaceRepository;
import com.iop.nexusmind.service.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;

    public FolderServiceImpl(FolderRepository folderRepository,
                            WorkspaceRepository workspaceRepository) {
        this.folderRepository = folderRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public List<Folder> getFoldersByWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("工作空间不存在"));
        return folderRepository.findByWorkspaceAndParentIsNull(workspace);
    }

    @Override
    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件夹不存在"));
    }

    @Override
    public Folder createFolder(Folder folder) {
        if (folder.getWorkspace() != null && folder.getWorkspace().getId() != null) {
            Workspace workspace = workspaceRepository.findById(folder.getWorkspace().getId())
                    .orElseThrow(() -> new RuntimeException("工作空间不存在"));
            folder.setWorkspace(workspace);
        }
        
        if (folder.getParent() != null && folder.getParent().getId() != null) {
            Folder parent = folderRepository.findById(folder.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("父文件夹不存在"));
            folder.setParent(parent);
        }
        
        return folderRepository.save(folder);
    }

    @Override
    public Folder updateFolder(Long id, Folder folderDetails) {
        Folder folder = getFolderById(id);
        
        folder.setName(folderDetails.getName());
        folder.setDescription(folderDetails.getDescription());
        
        return folderRepository.save(folder);
    }

    @Override
    public void deleteFolder(Long id) {
        Folder folder = getFolderById(id);
        folderRepository.delete(folder);
    }
}
