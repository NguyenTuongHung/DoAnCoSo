
/* =========================
   COMMENT SYSTEM
========================= */

const commentInput = document.querySelector(".comment-input-box textarea");
const sendBtn = document.querySelector(".comment-input-box button");
const commentList = document.querySelector(".comment-list");

// GỬI COMMENT
sendBtn.addEventListener("click", () => {
  const text = commentInput.value.trim();

  if (!text) {
    alert("Vui lòng nhập bình luận!");
    return;
  }

  const comment = createComment("User", text, "Vừa xong");

  commentList.prepend(comment);
  commentInput.value = "";
});

// TẠO COMMENT ELEMENT
function createComment(name, text, time) {
  const div = document.createElement("div");
  div.classList.add("comment-item");

  div.innerHTML = `
    <img src="https://i.pravatar.cc/46?u=${Math.random()}">

    <div class="comment-content">
      <h4>${name}</h4>
      <span>${time}</span>
      <p>${text}</p>

      <div class="comment-actions">
        <span class="like-btn">👍 Thích</span>
        <span class="reply-btn">↩ Trả lời</span>
      </div>
    </div>
  `;

  return div;
}

/* =========================
   LIKE COMMENT
========================= */

document.addEventListener("click", (e) => {
  if (e.target.classList.contains("like-btn")) {
    const btn = e.target;

    if (btn.classList.contains("liked")) {
      btn.classList.remove("liked");
      btn.style.color = "#2563eb";
      btn.textContent = "👍 Thích";
    } else {
      btn.classList.add("liked");
      btn.style.color = "red";
      btn.textContent = "❤️ Đã thích";
    }
  }
});

/* =========================
   SIMPLE REPLY (FAKE UX)
========================= */

document.addEventListener("click", (e) => {
  if (e.target.classList.contains("reply-btn")) {
    const commentBox = e.target.closest(".comment-item");

    const replyText = prompt("Nhập phản hồi:");

    if (replyText) {
      const reply = createComment("User", replyText, "Vừa xong");
      reply.style.marginLeft = "40px";
      reply.style.opacity = "0.9";

      commentBox.after(reply);
    }
  }
});

/* =========================
   AUTO SCROLL TO COMMENT
   (UX nhỏ nhưng giáo viên thích)
========================= */

sendBtn.addEventListener("click", () => {
  setTimeout(() => {
    commentList.scrollIntoView({ behavior: "smooth", block: "end" });
  }, 100);
});