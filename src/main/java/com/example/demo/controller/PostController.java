package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Category;
import com.example.demo.model.Comment;
import com.example.demo.model.History;
import com.example.demo.model.Post;
import com.example.demo.model.SavedNews;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SavedNewsRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

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

    @Autowired
private SavedNewsRepository savedNewsRepository;

@Autowired
private HistoryRepository historyRepository;

    // ================= TRANG CHỦ =================

@GetMapping("/")
public String getHomePage(Model model, HttpSession session) {

    // 1. Lấy tất cả bài viết gốc từ database
    List<Post> allPosts = postRepository.findAll();

    // 2. LỌC BÀI CHO TRANG CHỦ: Chỉ giữ lại bài viết KHÔNG THUỘC danh mục "Thảo luận"
    List<Post> homepagePosts = allPosts.stream()
            .filter(p -> p.getCategory() == null || !"Thảo luận".equals(p.getCategory().getName()))
            .collect(Collectors.toList());

    // 3. LỌC BÀI NỔI BẬT: Lấy từ danh sách trang chủ đã lọc để bài thảo luận không bị lọt lên slide to
    List<Post> featuredPosts = homepagePosts.stream()
            .limit(6)
            .collect(Collectors.toList());

    // 4. Đẩy dữ liệu sạch sang file index.html
    model.addAttribute("posts", homepagePosts);
    model.addAttribute("featuredPosts", featuredPosts);
    model.addAttribute("categories", categoryRepository.findAll());
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

        return "HoSoQuanTriVien/post-detail";
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
    // 1. Kiểm tra tài khoản admin dùng thử
    if ("admin".equals(username) && "123456".equals(password)) {
        session.setAttribute("userRole", "ADMIN");
        session.setAttribute("username", "Quản trị viên");
        session.setAttribute("userId", 0L); // Gán ID tạm cho admin
        return "redirect:/admin/dashboard";
    }

    // 2. Kiểm tra tài khoản trong database
    User user = userRepository.findByUsernameAndPassword(username, password);

    if (user != null) {
        // --- CHỖ NÀY BẠN ĐANG THIẾU DÒNG LƯU userId ---
        session.setAttribute("userId", user.getId()); 
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
        return "redirect:/login"; // Giữ nguyên vì login là URL Mapping
    }

    // ================= REGISTER =================

@GetMapping("/register")
public String showRegisterPage(Model model) {
    model.addAttribute("user", new User());
    // Bỏ "HoSoQuanTriVien/" vì file nằm ở thư mục gốc templates
    return "register"; 
}

@PostMapping("/register")
public String handleRegister(@ModelAttribute User user, Model model) {
    // 1. Kiểm tra username có bị trùng không
    if (userRepository.findByUsername(user.getUsername()) != null) {
        model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
        model.addAttribute("user", user); 
        // Bỏ "HoSoQuanTriVien/" ở đây nữa
        return "register";
    }

    // 2. Lưu user
    user.setRole("USER");
    userRepository.save(user);

    // 3. Chuyển hướng về trang đăng nhập
    return "redirect:/login?success";
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

        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/dashboardadmin";
    }

    // ================= CATEGORY =================
    @GetMapping("/admin/categories")
    public String showAllCategories(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("categories", categoryRepository.findAll());

        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/admin-categories";
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

        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/admin-posts";
    }

    @GetMapping("/admin/posts/add")
    public String showAddPostPage(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryRepository.findAll());

        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/add-post";
    }

    // ================= POSTS =================
    @GetMapping("/admin/posts/edit/{id}")
    public String showEditPostPage(@PathVariable Long id, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }

        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return "redirect:/admin/posts";
        }

        model.addAttribute("post", post);
        model.addAttribute("categories", categoryRepository.findAll());

        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/edit-post";
    }

    @PostMapping("/admin/posts/save")
public String savePost(@ModelAttribute Post post, @RequestParam(required = false) Long categoryId, HttpSession session) {
    // 1. Lấy thông tin tài khoản đang đăng nhập từ Session
    String username = (String) session.getAttribute("username");
    String userRole = (String) session.getAttribute("userRole");

    // Gán tên người đăng nhập vào trường Author (Tác giả) của bài viết
    if (username != null) {
        post.setAuthor(username);
    }

    // 2. TỰ ĐỘNG PHÂN LUỒNG BÀI VIẾT DỰA VÀO QUYỀN (ROLE)
    if ("ADMIN".equals(userRole)) {
        // Nếu là ADMIN đăng bài: Giữ nguyên logic cũ, gán danh mục được chọn từ giao diện quản trị
        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(post::setCategory);
        }
    } else {
        // Nếu là USER thường đăng bài: Ép bài viết vào danh mục "Thảo luận"
        Category discussionCat = categoryRepository.findByName("Thảo luận");
        
        // Phòng trường hợp trong database chưa có tên danh mục này, hệ thống sẽ tự tạo mới luôn
        if (discussionCat == null) {
            discussionCat = new Category();
            discussionCat.setName("Thảo luận");
            categoryRepository.save(discussionCat);
        }
        
        // Gắn danh mục Thảo luận vào bài viết của User
        post.setCategory(discussionCat);
    }

    // 3. Lưu bài viết vào Database
    postRepository.save(post);

    // 4. ĐIỀU HƯỚNG TRANG SAU KHI ĐĂNG THÀNH CÔNG
    if (!"ADMIN".equals(userRole)) {
        // Nếu là User thường, đăng xong đẩy họ thẳng sang trang riêng /thao-luan để xem bài viết
        return "redirect:/thao-luan";
    }
    
    // Nếu là Admin thì quay về danh sách quản lý bài viết của Admin
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
        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/admin-users";
    }

    @GetMapping("/admin/users/add")
    public String showAddUserPage(Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/add-user";
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
        // Đã cập nhật đường dẫn folder
        return "HoSoQuanTriVien/edit-user";
    }

    // ================= USERS =================
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
    public String showAllComments(@RequestParam(name = "keyword", required = false) String keyword, Model model, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";

        List<Comment> comments = (keyword != null && !keyword.isEmpty()) 
                                 ? commentRepository.findByContentContaining(keyword) 
                                 : commentRepository.findAll();

        model.addAttribute("comments", comments);
        return "HoSoQuanTriVien/admin-comments"; // Đã sửa đường dẫn
    }

    @GetMapping("/admin/comments/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        commentRepository.deleteById(id);
        return "redirect:/admin/comments";
    }

    // ================= SETTINGS =================
    @GetMapping("/admin/settings")
    public String showAdminSettings(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/login";
        return "HoSoQuanTriVien/admin-settings"; // Đã sửa đường dẫn
    }

   // ================= HỒ SƠ ADMIN =================
@GetMapping("/admin/ho-so")
public String showAdminProfile(Model model, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) return "redirect:/login";

    User user = userRepository.findById(userId).orElse(null);
    model.addAttribute("user", user);

    // TRỎ VÀO FILE HỒ SƠ CHỨ KHÔNG PHẢI FILE DANH SÁCH NGƯỜI DÙNG
    return "HoSoQuanTriVien/HoSoAdmin"; 
}

@PostMapping("/admin/profile/update")
public String updateProfile(@ModelAttribute("user") User userForm, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    if (userId == null) return "redirect:/login";

    User existingUser = userRepository.findById(userId).orElse(null);
    if (existingUser != null) {
        existingUser.setFullName(userForm.getFullName());
        existingUser.setEmail(userForm.getEmail());
        existingUser.setPhone(userForm.getPhone());
        existingUser.setLocation(userForm.getLocation());
        existingUser.setBirthDate(userForm.getBirthDate());
        existingUser.setGender(userForm.getGender());
        existingUser.setEducation(userForm.getEducation());

        userRepository.save(existingUser);
        session.setAttribute("username", existingUser.getUsername());
    }
    return "redirect:/admin/ho-so?success=true";
}
    // ================= COMMENTS POST =================
    @PostMapping("/tin-tuc/comment/save")
    public String saveComment(@RequestParam Long postId, @RequestParam String content, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUsername(username);
        comment.setPost(post);
        commentRepository.save(comment);
        
        return "redirect:/tin-tuc/" + postId;
    }


    

// 1. Hồ sơ của bạn
@GetMapping("/profile")
public String showUserProfile(Model model, HttpSession session) {
    Object userIdObj = session.getAttribute("userId");
    if (userIdObj == null) return "redirect:/login";

    Long userId = Long.valueOf(userIdObj.toString());
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return "redirect:/login";

    model.addAttribute("user", user);
    return "HoSoNguoiDung/profile";
}

// 2. Ý kiến của bạn
@GetMapping("/profile/comments")
public String showUserComments(Model model, HttpSession session) {
    String username = (String) session.getAttribute("username");
    if (username == null) return "redirect:/login";

    List<Comment> myComments = commentRepository.findByUsername(username);
    model.addAttribute("comments", myComments);
    
    // Đảm bảo có thông tin user để hiển thị Sidebar
    model.addAttribute("user", userRepository.findByUsername(username));
    
    return "HoSoNguoiDung/user-comments";
}

// Lấy danh sách tin đã lưu
@GetMapping("/saved-news")
public String showSavedNews(Model model, HttpSession session) {
    String username = (String) session.getAttribute("username");
    if (username == null) return "redirect:/login";

    // 1. Tìm đối tượng User từ username
    User user = userRepository.findByUsername(username);
    
    // 2. Tìm danh sách tin đã lưu bằng đối tượng User này
    List<SavedNews> savedList = savedNewsRepository.findByUser(user);
    
    model.addAttribute("savedNews", savedList);
    model.addAttribute("user", user);
    return "HoSoNguoiDung/saved-news";
}

// Lấy danh sách tin đã xem
@GetMapping("/history")
public String showHistory(Model model, HttpSession session) {
    String username = (String) session.getAttribute("username");
    if (username == null) return "redirect:/login";

    List<History> historyList = historyRepository.findByUsername(username);
    
    model.addAttribute("history", historyList);
    model.addAttribute("user", userRepository.findByUsername(username));
    return "HoSoNguoiDung/history";
}

// ================= ĐĂNG BÀI VIẾT MỚI (DÀNH CHO USER) =================

// 1. Hàm hiển thị giao diện viết bài mới (Khi user click vào nút Đăng bài)
@GetMapping("/create-post")
public String showCreatePostForm(Model model, HttpSession session) {
    String username = (String) session.getAttribute("username");
    if (username == null) {
        return "redirect:/login"; // Chưa đăng nhập thì đá về trang login
    }

    // Truyền một đối tượng Post trống vào form để binding dữ liệu
    model.addAttribute("post", new Post());
    
    // Lấy danh sách danh mục để user lựa chọn trong thẻ <select>
    model.addAttribute("categories", categoryRepository.findAll());
    
    // Lấy thông tin user hiện tại nếu giao diện đăng bài của bạn cần hiển thị Sidebar thông tin
    model.addAttribute("user", userRepository.findByUsername(username));

    // Trả về tên file HTML giao diện đăng bài của bạn (bỏ đuôi .html)
    // Nếu bạn cất file này trong thư mục HoSoNguoiDung thì sửa thành "HoSoNguoiDung/create-post"
    return "HoSoNguoiDung/create-post"; 
}


// ================= TRANG RIÊNG BIỆT DÀNH CHO THẢO LUẬN =================

@GetMapping("/thao-luan")
public String showDiscussionPage(Model model) {
    // 1. Lấy tất cả bài viết từ cơ sở dữ liệu
    List<Post> allPosts = postRepository.findAll();

    // 2. Lọc ra danh sách những bài viết thuộc danh mục mang tên "Thảo luận"
    List<Post> discussionPosts = allPosts.stream()
            .filter(p -> p.getCategory() != null && "Thảo luận".equals(p.getCategory().getName()))
            .collect(Collectors.toList());

    // 3. Đẩy danh sách vừa lọc qua biến "discussionPosts" để HTML hiển thị
    model.addAttribute("discussionPosts", discussionPosts);

    // 4. Trỏ trực tiếp về trang HTML độc lập mới tạo
    return "HoSoNguoiDung/thao-luan";
}


}

