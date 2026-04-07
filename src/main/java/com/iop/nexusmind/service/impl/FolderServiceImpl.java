package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.FolderRepository;
import com.iop.nexusmind.repository.WorkspaceRepository;
import com.iop.nexusmind.service.FolderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * 构造函数，注入依赖
     */
    public FolderServiceImpl(FolderRepository folderRepository,
                            WorkspaceRepository workspaceRepository) {
        this.folderRepository = folderRepository;
        this.workspaceRepository = workspaceRepository;
    }

    /**
     * 获取指定工作空间下的所有根文件夹（无父文件夹的文件夹）
     */
    @Override
    public List<Folder> getFoldersByWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("工作空间不存在"));
        return folderRepository.findByWorkspaceAndParentIsNull(workspace);
    }

    /**
     * 根据ID获取文件夹详情
     */
    @Override
    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文件夹不存在"));
    }

    /**
     * 创建新文件夹
     * 验证工作空间和父文件夹的存在性
     */
    @Override
    public Folder createFolder(Folder folder) {
        if (folder.getWorkspace() != null && folder.getWorkspace().getId() != null) {
            Workspace workspace = workspaceRepository.findById(folder.getWorkspace().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("工作空间不存在"));
            folder.setWorkspace(workspace);
        }
        
        if (folder.getParent() != null && folder.getParent().getId() != null) {
            Folder parent = folderRepository.findById(folder.getParent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("父文件夹不存在"));
            folder.setParent(parent);
        }
        
        return folderRepository.save(folder);
    }

    /**
     * 更新文件夹信息
     * 支持更新名称和描述
     */
    @Override
    public Folder updateFolder(Long id, Folder folderDetails) {
        Folder folder = getFolderById(id);
        
        folder.setName(folderDetails.getName());
        folder.setDescription(folderDetails.getDescription());
        
        return folderRepository.save(folder);
    }

    /**
     * 删除文件夹
     */
    @Override
    public void deleteFolder(Long id) {
        Folder folder = getFolderById(id);
        folderRepository.delete(folder);
    }
}
