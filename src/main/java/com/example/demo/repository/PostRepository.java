package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Category; // Đảm bảo import đúng model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Thêm dòng này:
    List<Post> findByCategory(Category category);
}