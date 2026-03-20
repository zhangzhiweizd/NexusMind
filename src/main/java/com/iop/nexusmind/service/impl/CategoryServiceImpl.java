package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.dto.CategoryDTO;
import com.iop.nexusmind.entity.Category;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.CategoryRepository;
import com.iop.nexusmind.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        log.info("查询所有分类，数量：{}", categories.size());
        return categories.stream()
            .map(category -> modelMapper.map(category, CategoryDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category saved = categoryRepository.save(category);
        log.info("创建分类成功：{}", saved.getName());
        return modelMapper.map(saved, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
        
        existing.setName(categoryDTO.getName());
        existing.setDescription(categoryDTO.getDescription());
        
        Category updated = categoryRepository.save(existing);
        log.info("更新分类成功：{}", updated.getName());
        return modelMapper.map(updated, CategoryDTO.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("分类不存在，ID: " + id));
        categoryRepository.delete(category);
        log.info("删除分类成功，ID: {}", id);
    }
}
