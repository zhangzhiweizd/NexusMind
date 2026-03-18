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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Note note = modelMapper.map(noteDTO, Note.class);
        
        if (noteDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(noteDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            note.setCategory(category);
        }
        
        if (noteDTO.getTagIds() != null && !noteDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(noteDTO.getTagIds()));
            note.setTags(tags);
        }
        
        Note savedNote = noteRepository.save(note);
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
        
        if (noteDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(noteDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existingNote.setCategory(category);
        }
        
        if (noteDTO.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(noteDTO.getTagIds()));
            existingNote.setTags(tags);
        }
        
        Note updatedNote = noteRepository.save(existingNote);
        return modelMapper.map(updatedNote, NoteDTO.class);
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
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
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
