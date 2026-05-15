
/* =========================
   LOGIN PAGE JS (FIXED)
========================= */

const loginForm = document.querySelector(".login-form");
const emailInput = document.querySelector('input[type="email"]');
const passwordInput = document.querySelector('input[type="password"]');

/* Submit login */
loginForm.addEventListener("submit", function (e) {
  e.preventDefault();

  const email = emailInput.value.trim();
  const password = passwordInput.value.trim();

  if (!email || !password) {
    alert("Vui lòng nhập đầy đủ email và mật khẩu!");
    return;
  }

  let user = null;

  /* ADMIN */
  if (email === "admin@gmail.com" && password === "123456") {
    user = {
      username: "Admin",
      email: email,
      role: "admin"
    };
  }

  /* USER */
  else if (email === "user@gmail.com" && password === "123456") {
    user = {
      username: "User",
      email: email,
      role: "user"
    };
  }

  else {
    alert("Sai tài khoản hoặc mật khẩu!");
    return;
  }

  /* SAVE USER TO LOCALSTORAGE */
  localStorage.setItem("user", JSON.stringify(user));

  alert("Đăng nhập thành công!");

  /* redirect về trang chính */
  window.location.href = "index.html";
});


/* =========================
   DEMO AUTO FILL
========================= */

const demoBox = document.querySelector(".demo-box");

if (demoBox) {
  demoBox.addEventListener("click", () => {
    emailInput.value = "admin@gmail.com";
    passwordInput.value = "123456";
    alert("Đã điền tài khoản Admin demo!");
  });
}


/* =========================
   ENTER KEY LOGIN
========================= */

document.addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    loginForm.dispatchEvent(new Event("submit"));
  }
});


/* =========================
   INPUT UI EFFECT
========================= */

const inputs = document.querySelectorAll("input");

inputs.forEach((input) => {
  input.addEventListener("focus", () => {
    input.style.borderColor = "#2563eb";
  });

  input.addEventListener("blur", () => {
    input.style.borderColor = "#d1d5db";
  });
});