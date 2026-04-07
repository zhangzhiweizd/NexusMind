package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色数据访问接口
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * 根据角色名称查找角色
     */
    Optional<Role> findByName(String name);
}
