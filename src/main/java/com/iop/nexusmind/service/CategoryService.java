package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.CategoryDTO;

import java.util.List;

/**
 * 分类服务接口
 * 定义分类管理的业务逻辑方法
 */
public interface CategoryService {
    
    List<CategoryDTO> findAll();
    
    CategoryDTO findById(Long id);
    
    CategoryDTO create(CategoryDTO categoryDTO);
    
    CategoryDTO update(Long id, CategoryDTO categoryDTO);
    
    void delete(Long id);
}
