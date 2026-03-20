package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    
    List<CategoryDTO> findAll();
    
    CategoryDTO findById(Long id);
    
    CategoryDTO create(CategoryDTO categoryDTO);
    
    CategoryDTO update(Long id, CategoryDTO categoryDTO);
    
    void delete(Long id);
}
