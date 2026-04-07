package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工作空间数据访问接口
 */
@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    
    /**
     * 根据创建者查找工作空间列表
     */
    List<Workspace> findByCreator(User creator);
    
    /**
     * 查找用户参与的工作空间列表
     */
    List<Workspace> findByMembersContaining(User member);
}
