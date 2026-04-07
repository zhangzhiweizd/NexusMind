package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Workspace;
import java.util.List;

public interface WorkspaceService {
    
    /**
     * 获取当前用户的所有工作空间
     * @return 工作空间列表
     */
    List<Workspace> getAllWorkspaces();
    
    /**
     * 根据ID获取工作空间详情
     * @param id 工作空间ID
     * @return 工作空间对象
     */
    Workspace getWorkspaceById(Long id);
    
    /**
     * 创建新工作空间
     * @param workspace 工作空间对象
     * @return 创建后的工作空间对象
     */
    Workspace createWorkspace(Workspace workspace);
    
    /**
     * 更新工作空间信息
     * @param id 工作空间ID
     * @param workspace 更新的工作空间信息
     * @return 更新后的工作空间对象
     */
    Workspace updateWorkspace(Long id, Workspace workspace);
    
    /**
     * 删除工作空间
     * @param id 工作空间ID
     */
    void deleteWorkspace(Long id);
}
