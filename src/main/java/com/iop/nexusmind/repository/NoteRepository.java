package com.iop.nexusmind.repository;

import com.iop.nexusmind.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    Page<Note> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT n FROM Note n WHERE n.content LIKE %:keyword% OR n.title LIKE %:keyword%")
    Page<Note> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Note> findByCategoryId(Long categoryId);
    
    List<Note> findByTagsId(Long tagId);
    
    Page<Note> findByIsPublicTrue(Pageable pageable);
}
