/* =========================
   ADMIN DASHBOARD JS
========================= */

/* Thông báo khi duyệt bài */
const approveButtons = document.querySelectorAll(".approve");

approveButtons.forEach((button) => {
  button.addEventListener("click", () => {
    alert("Bài viết đã được duyệt thành công!");
  });
});


/* Thông báo khi từ chối bài */
const rejectButtons = document.querySelectorAll(".reject");

rejectButtons.forEach((button) => {
  button.addEventListener("click", () => {
    const confirmReject = confirm("Bạn có chắc muốn từ chối bài viết này?");
    
    if (confirmReject) {
      alert("Bài viết đã bị từ chối.");
    }
  });
});


/* Quick Actions */
const quickButtons = document.querySelectorAll(".quick-actions button");

quickButtons.forEach((button) => {
  button.addEventListener("click", () => {
    alert(`Bạn vừa chọn: ${button.innerText}`);
  });
});


/* Notification button */
const notifyBtn = document.querySelector(".notify-btn");

notifyBtn.addEventListener("click", () => {
  alert("Bạn có 5 thông báo mới!");
});


/* Search box giả lập */
const searchInput = document.querySelector(".top-right input");

searchInput.addEventListener("keyup", (e) => {
  if (e.key === "Enter") {
    alert(`Tìm kiếm: ${searchInput.value}`);
  }
});


/* Sidebar active menu */
const menuLinks = document.querySelectorAll(".menu a");

menuLinks.forEach((link) => {
  link.addEventListener("click", () => {
    menuLinks.forEach((item) => item.classList.remove("active"));
    link.classList.add("active");
  });
});