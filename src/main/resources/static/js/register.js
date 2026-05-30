/* =========================
   REGISTER PAGE JS
========================= */

const registerForm = document.querySelector(".register-form");

const fullNameInput = document.querySelector('input[type="text"]');
const emailInput = document.querySelector('input[type="email"]');
const phoneInput = document.querySelectorAll('input[type="text"]')[1];
const passwordInput = document.querySelectorAll('input[type="password"]')[0];
const confirmPasswordInput = document.querySelectorAll('input[type="password"]')[1];
const roleSelect = document.querySelector("select");
const agreeCheckbox = document.querySelector('input[type="checkbox"]');

/* Submit register */
registerForm.addEventListener("submit", function (e) {
  e.preventDefault();

  const fullName = fullNameInput.value.trim();
  const email = emailInput.value.trim();
  const phone = phoneInput.value.trim();
  const password = passwordInput.value.trim();
  const confirmPassword = confirmPasswordInput.value.trim();
  const role = roleSelect.value;

  if (
    fullName === "" ||
    email === "" ||
    phone === "" ||
    password === "" ||
    confirmPassword === "" ||
    role === ""
  ) {
    alert("Vui lòng nhập đầy đủ thông tin!");
    return;
  }

  if (password.length < 6) {
    alert("Mật khẩu phải có ít nhất 6 ký tự!");
    return;
  }

  if (password !== confirmPassword) {
    alert("Mật khẩu xác nhận không khớp!");
    return;
  }

  if (!agreeCheckbox.checked) {
    alert("Bạn cần đồng ý với điều khoản sử dụng!");
    return;
  }

  /* Demo register success */
  alert("Đăng ký tài khoản thành công!");

  /* Redirect */
  window.location.href = "login.html";
});


/* Enter key support */
document.addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    registerForm.dispatchEvent(new Event("submit"));
  }
});


/* Focus effect */
const inputs = document.querySelectorAll("input, select");

inputs.forEach((input) => {
  input.addEventListener("focus", () => {
    input.style.borderColor = "#2563eb";
  });

  input.addEventListener("blur", () => {
    input.style.borderColor = "#d1d5db";
  });
});