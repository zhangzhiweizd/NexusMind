package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Folder;
import com.iop.nexusmind.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件夹数据访问接口
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    
    /**
     * 根据工作空间查找所有文件夹
     */
    List<Folder> findByWorkspace(Workspace workspace);
    
    /**
     * 根据父文件夹查找子文件夹列表
     */
    List<Folder> findByParent(Folder parent);
    
    /**
     * 根据工作空间查找根文件夹（无父文件夹的文件夹）
     */
    List<Folder> findByWorkspaceAndParentIsNull(Workspace workspace);
}
