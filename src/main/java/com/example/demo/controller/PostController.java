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

    // ================= TRANG CHỦ =================

    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session) {

        List<Post> allPosts = postRepository.findAll();

        model.addAttribute("posts", allPosts);
        model.addAttribute("categories", categoryRepository.findAll());

        model.addAttribute(
                "featuredPosts",
                allPosts.stream().limit(6).collect(Collectors.toList())
        );

        model.addAttribute("userRole", session.getAttribute("userRole"));

        return "index";
    }

    // ================= CHI TIẾT BÀI VIẾT =================

    @GetMapping("/tin-tuc/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return "redirect:/";
        }

        model.addAttribute("post", post);
        model.addAttribute("posts", postRepository.findAll());

        return "post-detail";
    }

    // ================= LOGIN =================

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        if ("admin".equals(username) && "123456".equals(password)) {

            session.setAttribute("userRole", "ADMIN");
            session.setAttribute("username", "Quản trị viên");

            return "redirect:/admin/dashboard";
        }

        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null) {

            session.setAttribute("userRole", user.getRole());
            session.setAttribute("username", user.getUsername());

            return "ADMIN".equals(user.getRole())
                    ? "redirect:/admin/dashboard"
                    : "redirect:/";
        }

        model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");

        return "login";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    // ================= REGISTER =================

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User user) {

        if (user != null) {

            user.setRole("USER");

            userRepository.save(user);
        }

        return "redirect:/login";
    }

    // ================= ADMIN DASHBOARD =================

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("totalPosts", postRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalComments", commentRepository.count());

        model.addAttribute(
                "posts",
                postRepository.findAll().stream().limit(5).collect(Collectors.toList())
        );

        return "dashboardadmin";
    }

    // ================= CATEGORY =================

    @GetMapping("/admin/categories")
    public String showAllCategories(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("categories", categoryRepository.findAll());

        return "admin-categories";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@ModelAttribute Category category) {

        categoryRepository.save(category);

        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {

        categoryRepository.deleteById(id);

        return "redirect:/admin/categories";
    }

    // ================= POSTS =================

    @GetMapping("/admin/posts")
    public String showAllPosts(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("posts", postRepository.findAll());

        return "admin-posts";
    }

    @GetMapping("/admin/posts/add")
    public String showAddPostPage(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryRepository.findAll());

        return "add-post";
    }

    @GetMapping("/admin/posts/edit/{id}")
    public String showEditPostPage(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return "redirect:/admin/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("categories", categoryRepository.findAll());

        return "edit-post";
    }

    @PostMapping("/admin/posts/save")
    public String savePost(
            @ModelAttribute Post post,
            @RequestParam(required = false) Long categoryId
    ) {

        if (categoryId != null) {

            categoryRepository.findById(categoryId)
                    .ifPresent(post::setCategory);
        }

        postRepository.save(post);

        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/posts/delete/{id}")
    public String deletePost(@PathVariable Long id) {

        postRepository.deleteById(id);

        return "redirect:/admin/posts";
    }

    // ================= USERS =================

    @GetMapping("/admin/users")
    public String showAllUsers(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("users", userRepository.findAll());

        return "admin-users";
    }

    @GetMapping("/admin/users/add")
    public String showAddUserPage(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("user", new User());

        return "add-user";
    }

    @GetMapping("/admin/users/edit/{id}")
public String showEditUserPage(@PathVariable Long id, Model model, HttpSession session) {

    if (!"ADMIN".equals(session.getAttribute("userRole"))) {
        return "redirect:/login";
    }

    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
        return "redirect:/admin/users";
    }

    model.addAttribute("user", user);

    return "edit-user";
}

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute User user) {

        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "redirect:/admin/users";
    }

    // ================= COMMENTS =================

    @GetMapping("/admin/comments")
    public String showAllComments(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("comments", commentRepository.findAll());

        return "admin-comments";
    }

    // ================= SETTINGS =================

    @GetMapping("/admin/settings")
    public String showAdminSettings(HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        return "admin-settings";
    }

    // ================= PROFILE =================

    @PostMapping("/admin/profile/update")
    public String updateAdminProfile(
            @ModelAttribute User user,
            HttpSession session
    ) {

        Long userId = user.getId();

        if (userId != null) {

            userRepository.findById(userId).ifPresent(existingUser -> {

                existingUser.setEmail(user.getEmail());

                if (user.getPassword() != null
                        && !user.getPassword().isEmpty()) {

                    existingUser.setPassword(user.getPassword());
                }

                userRepository.save(existingUser);
            });
        }

        return "redirect:/admin/profile?success=true";
    }
}