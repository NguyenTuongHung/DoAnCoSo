document.addEventListener("DOMContentLoaded", function () {
  const navRight = document.getElementById("navRight");
  if (!navRight) return;

  function getUser() {
    return JSON.parse(localStorage.getItem("user"));
  }

  function renderNavbar() {
    const user = getUser();

    if (!user) {
      navRight.innerHTML = `
        <button onclick="location.href='login.html'">Đăng nhập</button>
        <button onclick="location.href='register.html'">Đăng ký</button>
      `;
      return;
    }

    if (user.role === "admin") {
      navRight.innerHTML = `
        <span>👑 ${user.username}</span>
        <button onclick="location.href='dashboardadmin.html'">Dashboard</button>
        <button onclick="logout()">Đăng xuất</button>
      `;
    } else {
      navRight.innerHTML = `
        <span>👤 ${user.username}</span>
        <button onclick="location.href='create-post.html'">Đăng bài</button>
        <button onclick="logout()">Đăng xuất</button>
      `;
    }
  }

  window.logout = function () {
    localStorage.removeItem("user");
    location.href = "index.html";
  };

  renderNavbar();
});