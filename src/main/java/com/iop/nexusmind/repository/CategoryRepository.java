package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 分类数据访问层接口
 * 提供分类的数据库操作方法
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    java.util.List<Category> findByParentId(Long parentId);
    
    java.util.List<Category> findByParentIsNull();
}
