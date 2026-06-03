package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Phương thức hiện có của bạn
    List<Post> findByCategory(Category category);
    
    // Thêm phương thức này để phục vụ mục "Tin hot trong tuần"
    // Lưu ý: Nếu trong model Post của bạn thuộc tính là createdAt, hãy dùng OrderByCreatedAtDesc
    // Nếu là createdDate, hãy dùng OrderByCreatedDateDesc
    List<Post> findTop3ByOrderByCreatedDateDesc();
}