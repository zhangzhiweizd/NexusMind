package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.dto.NoteDTO;
import com.iop.nexusmind.entity.Category;
import com.iop.nexusmind.entity.Note;
import com.iop.nexusmind.entity.Tag;
import com.iop.nexusmind.exception.ResourceNotFoundException;
import com.iop.nexusmind.repository.CategoryRepository;
import com.iop.nexusmind.repository.NoteRepository;
import com.iop.nexusmind.repository.TagRepository;
import com.iop.nexusmind.service.AIService;
import com.iop.nexusmind.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AIService aiService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public NoteDTO createNote(NoteDTO noteDTO) {
        log.info("创建新笔记：{}", noteDTO.getTitle());
        Note note = modelMapper.map(noteDTO, Note.class);
        bindCategoryAndTags(note, noteDTO);
        Note savedNote = noteRepository.save(note);
        log.info("笔记创建成功，ID: {}", savedNote.getId());
        return modelMapper.map(savedNote, NoteDTO.class);
    }

    @Override
    @Transactional
    public NoteDTO updateNote(Long id, NoteDTO noteDTO) {
        Note existingNote = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        existingNote.setTitle(noteDTO.getTitle());
        existingNote.setContent(noteDTO.getContent());
        existingNote.setIsPublic(noteDTO.getIsPublic());
        
        bindCategoryAndTags(existingNote, noteDTO);
        
        Note updatedNote = noteRepository.save(existingNote);
        return modelMapper.map(updatedNote, NoteDTO.class);
    }

    /**
     * 绑定分类和标签到笔记
     */
    private void bindCategoryAndTags(Note note, NoteDTO noteDTO) {
        if (noteDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(noteDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + noteDTO.getCategoryId()));
            note.setCategory(category);
        }
        
        if (noteDTO.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(noteDTO.getTagIds()));
            if (tags.size() != noteDTO.getTagIds().size()) {
                log.warn("部分标签未找到，期望：{}, 实际：{}", noteDTO.getTagIds().size(), tags.size());
            }
            note.setTags(tags);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDTO getNoteById(Long id) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return modelMapper.map(note, NoteDTO.class);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        log.info("删除笔记，ID: {}", id);
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        noteRepository.delete(note);
        log.info("笔记删除成功，ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> getAllNotes(Pageable pageable) {
        return noteRepository.findAll(pageable).map(note -> modelMapper.map(note, NoteDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> searchNotes(String keyword, Pageable pageable) {
        return noteRepository.searchByKeyword(keyword, pageable)
            .map(note -> modelMapper.map(note, NoteDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> getNotesByCategory(Long categoryId) {
        List<Note> notes = noteRepository.findByCategoryId(categoryId);
        return notes.stream()
            .map(note -> modelMapper.map(note, NoteDTO.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> getNotesByTag(Long tagId) {
        List<Note> notes = noteRepository.findByTagsId(tagId);
        return notes.stream()
            .map(note -> modelMapper.map(note, NoteDTO.class))
            .toList();
    }

    @Override
    public String generateSummary(String content) {
        return aiService.generateSummary(content);
    }
}
