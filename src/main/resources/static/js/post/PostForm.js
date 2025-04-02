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