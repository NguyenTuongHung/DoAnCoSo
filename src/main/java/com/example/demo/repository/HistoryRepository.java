package com.example.demo.repository;

import com.example.demo.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    
    List<History> findByUsernameOrderByViewedAtDesc(String username);

    // THÊM ĐOẠN NÀY ĐỂ DỌN DẸP LỊCH SỬ KHI BÀI VIẾT BỊ XÓA
    @Transactional
    @Modifying
    @Query("DELETE FROM History h WHERE h.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}