package com.iop.nexusmind.service;

import com.iop.nexusmind.entity.Folder;
import java.util.List;

public interface FolderService {
    
    /**
     * 获取指定工作空间下的所有根文件夹
     * @param workspaceId 工作空间ID
     * @return 文件夹列表
     */
    List<Folder> getFoldersByWorkspace(Long workspaceId);
    
    /**
     * 根据ID获取文件夹详情
     * @param id 文件夹ID
     * @return 文件夹对象
     */
    Folder getFolderById(Long id);
    
    /**
     * 创建新文件夹
     * @param folder 文件夹对象
     * @return 创建后的文件夹对象
     */
    Folder createFolder(Folder folder);
    
    /**
     * 更新文件夹信息
     * @param id 文件夹ID
     * @param folder 更新的文件夹信息
     * @return 更新后的文件夹对象
     */
    Folder updateFolder(Long id, Folder folder);
    
    /**
     * 删除文件夹
     * @param id 文件夹ID
     */
    void deleteFolder(Long id);
}
