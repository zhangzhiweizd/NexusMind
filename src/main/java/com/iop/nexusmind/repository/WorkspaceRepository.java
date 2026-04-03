package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Workspace;
import com.iop.nexusmind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    
    List<Workspace> findByCreator(User creator);
    
    List<Workspace> findByMembersContaining(User member);
}
