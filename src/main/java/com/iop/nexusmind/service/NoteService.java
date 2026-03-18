package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.NoteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {
    
    NoteDTO createNote(NoteDTO noteDTO);
    
    NoteDTO updateNote(Long id, NoteDTO noteDTO);
    
    NoteDTO getNoteById(Long id);
    
    void deleteNote(Long id);
    
    Page<NoteDTO> getAllNotes(Pageable pageable);
    
    Page<NoteDTO> searchNotes(String keyword, Pageable pageable);
    
    List<NoteDTO> getNotesByCategory(Long categoryId);
    
    List<NoteDTO> getNotesByTag(Long tagId);
    
    String generateSummary(String content);
}
