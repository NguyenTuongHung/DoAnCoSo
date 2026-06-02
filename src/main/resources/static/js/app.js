// =========================
// DỮ LIỆU GIẢ LẬP BÀI VIẾT
// =========================

const data = [
  {
    title: "Tin 1",
    content: "Nội dung tin 1"
  },
  {
    title: "Tin 2",
    content: "Nội dung tin 2"
  },
  {
    title: "Tin 3",
    content: "Nội dung tin 3"
  },
  {
    title: "Tin 4",
    content: "Nội dung tin 4"
  }
];


// =========================
// GIẢ LẬP ĐĂNG NHẬP
// =========================

// null = chưa đăng nhập

let user = null;

// test USER:
// let user = {
//   name: "Hung",
//   role: "USER"
// };

// test ADMIN:
// let user = {
//   name: "Admin",
//   role: "ADMIN"
// };


// =========================
// HIỂN THỊ BÀI VIẾT
// =========================

function renderPosts() {
  const posts = document.getElementById("posts");
  const hot = document.getElementById("hot");

  posts.innerHTML = "";
  hot.innerHTML = "";

  data.forEach((item, index) => {
    posts.innerHTML += `
      <div class="card">
        <img src="https://picsum.photos/300/200?random=${index}">
        <h3>${item.title}</h3>
        <p>${item.content}</p>
      </div>
    `;

    hot.innerHTML += `
      <li>${item.title}</li>
    `;
  });
}


// =========================
// HIỂN THỊ NAVBAR THEO ROLE
// =========================

function renderNavbar() {
  const nav = document.getElementById("navRight");

  // chưa login
  if (!user) {
    nav.innerHTML = `
      <button class="login-btn">Đăng nhập</button>
      <button class="register-btn">Đăng ký</button>
    `;
  }

  // admin
  else if (user.role === "ADMIN") {
    nav.innerHTML = `
      <span>Xin chào, ${user.name}</span>
      <button>Dashboard</button>
      <button class="logout-btn" onclick="logout()">
        Logout
      </button>
    `;
  }

  // user thường
  else {
    nav.innerHTML = `
      <span>Xin chào, ${user.name}</span>
      <button>Đăng bài</button>
      <button class="logout-btn" onclick="logout()">
        Logout
      </button>
    `;
  }
}


// =========================
// LOGOUT
// =========================

function logout() {
  user = null;
  renderNavbar();
}


// =========================
// CHẠY KHI LOAD TRANG
// =========================

renderPosts();
renderNavbar();