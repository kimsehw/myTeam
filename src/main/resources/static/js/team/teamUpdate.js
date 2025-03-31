document.addEventListener("DOMContentLoaded", function () {
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.getElementById("cancelButton");

    const teamNameText = document.getElementById("teamNameText");
    const teamDetailText = document.getElementById("teamDetailText");
    const regionText = document.getElementById("regionText");
    const ageRangeText = document.getElementById("ageRangeText");
    const logoShow = document.getElementById("logoShow");

    const teamNameInput = document.getElementById("teamNameInput");
    const teamDetailInput = document.getElementById("teamDetailInput");
    const regionSelect = document.getElementById("regionSelect");
    const ageRangeSelect = document.getElementById("ageRangeSelect");
    const selectContainer = document.getElementById("selectContainer");
    const teamLogoFile = document.getElementById("teamLogoFile");
    const logoInputContainer = document.getElementById("logoInputContainer");

    // "팀 정보 수정" 버튼 클릭 시 입력 필드 활성화
    editButton.addEventListener("click", function () {
        teamNameText.classList.add("hidden");
        teamDetailText.classList.add("hidden");
        regionText.classList.add("hidden");
        ageRangeText.classList.add("hidden");
        logoShow.classList.add("hidden");

        teamNameInput.classList.remove("hidden");
        teamDetailInput.classList.remove("hidden");
        selectContainer.classList.remove("hidden");
        regionSelect.classList.remove("hidden");
        ageRangeSelect.classList.remove("hidden");
        teamLogoFile.classList.remove("hidden");
        logoInputContainer.classList.remove("hidden");

        editButton.classList.add("hidden");
        saveButton.classList.remove("hidden");
        cancelButton.classList.remove("hidden");
    });

    // "취소" 버튼 클릭 시 원래 상태로 복구
    cancelButton.addEventListener("click", function () {
        teamNameText.classList.remove("hidden");
        teamDetailText.classList.remove("hidden");
        regionText.classList.remove("hidden");
        ageRangeText.classList.remove("hidden");
        logoShow.classList.remove("hidden");

        teamNameInput.classList.add("hidden");
        teamDetailInput.classList.add("hidden");
        selectContainer.classList.add("hidden");
        regionSelect.classList.add("hidden");
        ageRangeSelect.classList.add("hidden");
        teamLogoFile.classList.add("hidden");
        logoInputContainer.classList.add("hidden");

        editButton.classList.remove("hidden");
        saveButton.classList.add("hidden");
        cancelButton.classList.add("hidden");
    });
});