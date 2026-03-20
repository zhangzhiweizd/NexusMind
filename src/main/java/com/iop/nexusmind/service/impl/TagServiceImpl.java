package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.dto.TagDTO;
import com.iop.nexusmind.entity.Tag;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.TagRepository;
import com.iop.nexusmind.service.TagService;
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
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> findAll() {
        List<Tag> tags = tagRepository.findAll();
        log.info("查询所有标签，数量：{}", tags.size());
        return tags.stream()
            .map(tag -> modelMapper.map(tag, TagDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO findById(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("标签不存在，ID: " + id));
        return modelMapper.map(tag, TagDTO.class);
    }

    @Override
    @Transactional
    public TagDTO create(TagDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        Tag saved = tagRepository.save(tag);
        log.info("创建标签成功：{}", saved.getName());
        return modelMapper.map(saved, TagDTO.class);
    }

    @Override
    @Transactional
    public TagDTO update(Long id, TagDTO tagDTO) {
        Tag existing = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("标签不存在，ID: " + id));
        
        existing.setName(tagDTO.getName());
        existing.setColor(tagDTO.getColor());
        
        Tag updated = tagRepository.save(existing);
        log.info("更新标签成功：{}", updated.getName());
        return modelMapper.map(updated, TagDTO.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("标签不存在，ID: " + id));
        tagRepository.delete(tag);
        log.info("删除标签成功，ID: {}", id);
    }
}
