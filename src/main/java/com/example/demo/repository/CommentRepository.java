package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Hàm này giúp lọc dữ liệu chứa từ khóa
    List<Comment> findByContentContaining(String keyword);

    // Hàm này giúp lấy tất cả bình luận của một người dùng dựa vào username
    List<Comment> findByUsername(String username);
}