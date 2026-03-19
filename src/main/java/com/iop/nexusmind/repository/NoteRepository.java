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
    
    // 添加全文搜索索引（在实体类中已经优化）
    @Query("SELECT n FROM Note n WHERE LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Note> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Note> findByCategoryId(Long categoryId);
    
    List<Note> findByTagsId(Long tagId);
    
    Page<Note> findByIsPublicTrue(Pageable pageable);
    
    // 添加按创建时间排序的查询
    Page<Note> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 添加按更新时间排序的查询
    Page<Note> findAllByOrderByUpdatedAtDesc(Pageable pageable);

}
