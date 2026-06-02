package com.example.demo.model;

import java.util.List; // Import để chống lặp vô hạn

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Một chuyên mục có thể có nhiều bài viết
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Ngăn chặn lỗi lặp vô hạn khi map dữ liệu qua lại với Post
    private List<Post> posts;

    // ===== Constructor không tham số bắt buộc của JPA =====
    public Category() {}

    // ===== Constructor có tham số (Tiện dùng khi khởi tạo nhanh) =====
    public Category(String name) {
        this.name = name;
    }

    // ===== Getters and Setters =====
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }

    // ĐÃ BỔ SUNG: Getter & Setter cho danh sách bài viết thuộc danh mục
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}