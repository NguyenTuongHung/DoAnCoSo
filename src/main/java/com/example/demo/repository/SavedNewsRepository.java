package com.example.demo.repository;

import com.example.demo.model.SavedNews;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SavedNewsRepository extends JpaRepository<SavedNews, Long> {
    
    // Sửa thành tìm theo đối tượng User (Spring Data JPA sẽ tự hiểu)
    List<SavedNews> findByUser(User user);
    
    // Hoặc nếu bạn muốn tìm theo username (String):
    List<SavedNews> findByUser_Username(String username);

    boolean existsByUserAndPost_Id(User user, Long postId);
}