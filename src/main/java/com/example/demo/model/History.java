package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history") // Đặt tên bảng trong database là 'history'
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lưu username để biết ai đã xem
    private String username;

    // Liên kết với bài viết đã xem
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Thời gian xem tin
    private LocalDateTime viewedAt;

    // Constructor mặc định (bắt buộc cho JPA)
    public History() {
        this.viewedAt = LocalDateTime.now(); // Tự động lấy thời gian hiện tại
    }

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
}