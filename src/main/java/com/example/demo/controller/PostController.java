package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.Category;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    // ================= TRANG CHỦ (FRONT-END) =================
    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session) {
        List<Post> allPosts = postRepository.findAll();
        model.addAttribute("posts", allPosts);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("featuredPosts", allPosts.stream().limit(6).collect(Collectors.toList()));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "index";
    }

    // ================= ĐĂNG NHẬP / ĐĂNG XUẤT =================
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, 
                             @RequestParam String password, 
                             HttpSession session, 
                             Model model) {
        if ("admin".equals(username) && "123456".equals(password)) {
            session.setAttribute("userRole", "ADMIN");
            session.setAttribute("username", "Quản trị viên");
            return "redirect:/admin/dashboard"; 
        } 
        
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("username", user.getUsername());
            return "ADMIN".equals(user.getRole()) ? "redirect:/admin/dashboard" : "redirect:/";
        } else {
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ================= ADMIN DASHBOARD =================
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        
        if (categoryRepository.count() == 0) {
            Category defaultCat = new Category();
            defaultCat.setName("Tin tức chung");
            categoryRepository.save(defaultCat);
        }

        // Dashboard chỉ lấy 5 bài mới nhất để hiển thị tóm tắt
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts.stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        
        return "dashboardadmin";
    }

    // ================= QUẢN LÝ BÀI VIẾT (FIXED) =================
    
    // Hàm này dùng để hiển thị TRANG DANH SÁCH BÀI VIẾT RIÊNG BIỆT
    @GetMapping("/admin/posts")
    public String showAllPosts(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        
        model.addAttribute("posts", postRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        
        return "admin-posts"; // Phải có file admin-posts.html trong templates
    }

    @GetMapping("/admin/posts/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        return "add-post";
    }

    @PostMapping("/admin/posts/save")
    public String savePost(@ModelAttribute("post") Post post, 
                           @RequestParam(value = "categoryId", required = false) Long categoryId, 
                           HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        
        if (post != null && post.getTitle() != null && !post.getTitle().trim().isEmpty()) {
            if (categoryId != null) {
                categoryRepository.findById(categoryId).ifPresent(post::setCategory);
            }
            postRepository.save(post);
        }
        return "redirect:/admin/posts"; // Lưu xong quay về danh sách bài viết, không về dashboard
    }

    @GetMapping("/admin/posts/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole")) || id == null) return "redirect:/admin/posts";
        
        postRepository.findById(id).ifPresent(post -> {
            model.addAttribute("post", post);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("totalPosts", postRepository.count());
        });
        return model.containsAttribute("post") ? "add-post" : "redirect:/admin/posts";
    }

    @GetMapping("/admin/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole")) && id != null) {
            postRepository.deleteById(id);
        }
        return "redirect:/admin/posts"; // Xóa xong ở lại trang danh sách bài viết
    }

    // ================= QUẢN LÝ CHUYÊN MỤC =================
    @GetMapping("/admin/categories")
    public String showCategories(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        return "admin-categories";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@RequestParam(required = false) Long id, @RequestParam String name, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole"))) {
            Category cat = new Category();
            if (id != null) cat.setId(id);
            cat.setName(name);
            categoryRepository.save(cat);
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole")) && id != null) {
            categoryRepository.deleteById(id);
        }
        return "redirect:/admin/categories";
    }

    // ================= QUẢN LÝ NGƯỜI DÙNG =================
    @GetMapping("/admin/users")
    public String showUserManagement(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        return "admin-users"; 
    }

    @GetMapping("/admin/users/add")
    public String showAddUserForm(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("user", new User());
        model.addAttribute("totalPosts", postRepository.count());
        return "add-user";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole")) || id == null) return "redirect:/admin/users";
        
        userRepository.findById(id).ifPresent(user -> {
            model.addAttribute("user", user);
            model.addAttribute("totalPosts", postRepository.count());
        });
        return model.containsAttribute("user") ? "add-user" : "redirect:/admin/users";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute("user") User user, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole")) && user != null) {
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole")) && id != null) {
            userRepository.deleteById(id);
        }
        return "redirect:/admin/users";
    }

    // ================= QUẢN LÝ BÌNH LUẬN =================
    @GetMapping("/admin/comments")
    public String showComments(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        
        model.addAttribute("comments", commentRepository.findAll());
        model.addAttribute("totalComments", commentRepository.count());
        model.addAttribute("totalPosts", postRepository.count());
        
        return "admin-comments";
    }

    @GetMapping("/admin/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id, HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("userRole")) && id != null) {
            commentRepository.deleteById(id);
        }
        return "redirect:/admin/comments";
    }

    // ================= CÀI ĐẶT =================
    @GetMapping("/admin/settings")
    public String showSettings(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalComments", commentRepository.count());
        return "admin-settings";
    }

    // ================= CHI TIẾT BÀI VIẾT =================
    @GetMapping("/tin-tuc/{id}")
    public String getPostDetail(@PathVariable("id") Long id, Model model) {
        if (id == null) return "redirect:/";
        postRepository.findById(id).ifPresent(post -> {
            model.addAttribute("post", post);
            model.addAttribute("posts", postRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
        });
        return model.containsAttribute("post") ? "post-detail" : "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
}