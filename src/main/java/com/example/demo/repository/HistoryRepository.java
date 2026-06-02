package com.example.demo.repository;

import com.example.demo.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    
    // Thêm hàm này vào để Controller có thể sử dụng
    List<History> findByUsernameOrderByViewedAtDesc(String username);
    
}