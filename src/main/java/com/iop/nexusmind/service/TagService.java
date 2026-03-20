package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.TagDTO;

import java.util.List;

public interface TagService {
    
    List<TagDTO> findAll();
    
    TagDTO findById(Long id);
    
    TagDTO create(TagDTO tagDTO);
    
    TagDTO update(Long id, TagDTO tagDTO);
    
    void delete(Long id);
}
