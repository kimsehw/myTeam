document.addEventListener("DOMContentLoaded", function () {
    const postTypeSelect = document.querySelector("select[name='postType']");
    const titleInput = document.querySelector("input[name='title']");
    const extraTexts = document.querySelectorAll("p[name='extraText']");
    const detailInput = document.getElementById("detail");
    const teamSelect = document.getElementById("teamSelect");


    function toggleTitleInput() {
        if (!postTypeSelect.value) {
            titleInput.disabled = true;
            titleInput.value = "";
            detailInput.disabled = true;
            detailInput.value = "";
            teamSelect.disabled = true;
            teamSelect.value = "";
            extraTexts.forEach(el => el.classList.remove("hidden"));
        } else {
            titleInput.disabled = false;
            detailInput.disabled = false;
            teamSelect.disabled = false;
            extraTexts.forEach(el => el.classList.add("hidden"));
        }
    }

    // 페이지 로드 시 초기 상태 설정
    toggleTitleInput();

    // postType 변경 감지하여 title 입력 필드 상태 업데이트
    postTypeSelect.addEventListener("change", toggleTitleInput);
});
// th:action="@{/posts/new/{postId}(postId=${postId})}" method="post"

function postUpdate(postId) {

    var url = `/posts/new/${postId}`

    const formData = {
        title: document.getElementById('title').value,
        detail: document.getElementById('detail').value,
        postType: document.getElementById('postType').value,
        teamId: document.getElementById('teamSelect').value
    }
    document.querySelectorAll(".fieldError").forEach(el => {
        el.textContent = "";
        el.style.display = "none";
    });

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            alert("글을 수정하였습니다.");
            window.location.href = '/posts/' + postId;
        } else {
            return response.json();
        }
    })
    .then(errors => {
        if (errors) {
            Object.keys(errors).forEach(field => {
                const errorMessage = errors[field];
                const errorElement = document.getElementById(field + "Error");
                if (errorElement) {
                    errorElement.textContent = errorMessage;
                    errorElement.style.display = "block";
                }
            });
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert('초대 요청 중 오류가 발생했습니다.');
    });
}