package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Hàm này giúp lọc dữ liệu chứa từ khóa
    List<Comment> findByContentContaining(String keyword);

    // Hàm này giúp lấy tất cả bình luận của một người dùng dựa vào username
    List<Comment> findByUsername(String username);

    // THÊM ĐOẠN NÀY ĐỂ XÓA CÁC BÌNH LUẬN LIÊN QUAN TRƯỚC KHI XÓA BÀI VIẾT
    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}