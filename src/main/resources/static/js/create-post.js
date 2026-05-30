/* =========================
   CREATE POST PAGE JS
========================= */

/* Form submit */
const postForm = document.querySelector(".post-form");

postForm.addEventListener("submit", function (e) {
  e.preventDefault();

  const title = document.querySelector('input[type="text"]').value;

  if (title.trim() === "") {
    alert("Vui lòng nhập tiêu đề bài viết!");
    return;
  }

  alert("Bài viết đã được gửi thành công và đang chờ admin duyệt!");
  postForm.reset();
});


/* Lưu nháp */
const draftBtn = document.querySelector(".draft-btn");

draftBtn.addEventListener("click", () => {
  alert("Bài viết đã được lưu vào bản nháp!");
});


/* Preview tên file ảnh đại diện */
const fileInputs = document.querySelectorAll('input[type="file"]');

fileInputs.forEach((input) => {
  input.addEventListener("change", () => {
    if (input.files.length > 0) {
      alert(`Đã chọn file: ${input.files[0].name}`);
    }
  });
});


/* Auto save giả lập sau 30 giây */
setInterval(() => {
  console.log("Tự động lưu nháp...");
}, 30000);


/* Highlight textarea khi focus */
const textareas = document.querySelectorAll("textarea");

textareas.forEach((textarea) => {
  textarea.addEventListener("focus", () => {
    textarea.style.borderColor = "#2563eb";
  });

  textarea.addEventListener("blur", () => {
    textarea.style.borderColor = "#d1d5db";
  });
});


/* Quay về trang chủ xác nhận */
const homeBtn = document.querySelector(".home-btn");

homeBtn.addEventListener("click", function (e) {
  const confirmLeave = confirm(
    "Bạn có chắc muốn quay về trang chủ? Nội dung chưa lưu có thể bị mất."
  );

  if (!confirmLeave) {
    e.preventDefault();
  }
});