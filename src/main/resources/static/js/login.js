/* =========================
   LOGIN PAGE JS
========================= */

const loginForm = document.querySelector(".login-form");
const emailInput = document.querySelector('input[type="email"]');
const passwordInput = document.querySelector('input[type="password"]');

/* Submit login */
loginForm.addEventListener("submit", function (e) {
  e.preventDefault();

  const email = emailInput.value.trim();
  const password = passwordInput.value.trim();

  if (email === "" || password === "") {
    alert("Vui lòng nhập đầy đủ email và mật khẩu!");
    return;
  }

  /* Demo role login */
  if (email === "admin@gmail.com" && password === "123456") {
    alert("Đăng nhập Admin thành công!");
    window.location.href = "admin-dashboard.html";
    return;
  }

  if (email === "user@gmail.com" && password === "123456") {
    alert("Đăng nhập User thành công!");
    window.location.href = "create-post.html";
    return;
  }

  alert("Sai tài khoản hoặc mật khẩu!");
});


/* Auto fill demo account when click demo box */
const demoBox = document.querySelector(".demo-box");

demoBox.addEventListener("click", () => {
  emailInput.value = "admin@gmail.com";
  passwordInput.value = "123456";

  alert("Đã tự động điền tài khoản Admin demo!");
});


/* Enter key support */
document.addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    loginForm.dispatchEvent(new Event("submit"));
  }
});


/* Focus effect */
const inputs = document.querySelectorAll("input");

inputs.forEach((input) => {
  input.addEventListener("focus", () => {
    input.style.borderColor = "#2563eb";
  });

  input.addEventListener("blur", () => {
    input.style.borderColor = "#d1d5db";
  });
});