function addChat(postId, parentId) {

    var url = `/posts/${postId}/addChat`
    var content;
    if (parentId) {
        content = document.querySelector(`#replyForm-${parentId} .reply-content`).value;
    } else {
        content = document.getElementById('content').value;
    }

    const formData = {
                parentId: parentId,
                content: content
            };

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            alert("댓글 등록 완료");
            document.getElementById('content').value = "";
            window.location.reload();
        } else {
            return response.text();
        }
    })
    .then(data => {
        if (data) {
            alert(data)
        }
    })
    .catch(error => {
        console.error("Error:", error)
        alert("댓글 등록 중 오류 발생 ", error)
    });
}

function showReplyForm(chatId) {
    var replyForm = document.getElementById(`replyForm-${chatId}`);
    if (replyForm.classList.contains("hidden")) {
        replyForm.classList.remove("hidden");
    } else {
        replyForm.classList.add("hidden");
    }
}