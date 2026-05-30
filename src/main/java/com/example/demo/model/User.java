package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    // Các thuộc tính mới bạn cần cho trang cá nhân
    private String phone;
    private String location;

    // Vai trò người dùng: "ADMIN" hoặc "USER"
    private String role;

    private String fullName;
    private String birthDate; // Có thể dùng LocalDate
    private String gender;
    private String education;

    public User() {
    }

    public User(String username, String password, String email, String role, String phone, String location) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.location = location;
    }

    // Các Getter và Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Getter và Setter cho Phone
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // Getter và Setter cho Location
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
}