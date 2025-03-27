document.addEventListener("DOMContentLoaded", function () {
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.getElementById("cancelButton");
    const deleteButtons = document.querySelectorAll("#deleteModalButton");
    const viewModes = document.querySelectorAll(".view-mode")
    const editModes = document.querySelectorAll(".edit-mode")

    editButton.addEventListener("click", function () {
        editButton.classList.add("hidden");

        viewModes.forEach(el => el.classList.add("hidden"));
        editModes.forEach(el => el.classList.remove("hidden"));
        deleteButtons.forEach(el => el.classList.remove("hidden"));

        saveButton.classList.remove("hidden");
        cancelButton.classList.remove("hidden");
    });

    cancelButton.addEventListener("click", function () {
        editButton.classList.remove("hidden");

        viewModes.forEach(el => el.classList.remove("hidden"));
        editModes.forEach(el => el.classList.add("hidden"));
        deleteButtons.forEach(el => el.classList.add("hidden"));

        saveButton.classList.add("hidden");
        cancelButton.classList.add("hidden");
    });

//  수정 요청 -> PATCH 요청
    saveButton.addEventListener("click", function() {
        let teamMemberUpdateDtos = [];

        document.querySelectorAll("tr[data-teamMem-id]").forEach(row => {
            let teamMemId = row.getAttribute("data-teamMem-id");

            let originalTeamRole = row.getAttribute("data-origin-teamRole");
            let originalPlayerNum = row.getAttribute("data-origin-playerNum");
            let originalName = row.getAttribute("data-origin-name");
            let originalPosition = row.getAttribute("data-origin-position");

            let updatedTeamRole = row.querySelector(".teamRoleInput").value;
            let updatedPlayerNum = row.querySelector(".playerNumInput").value;
            let updatedName = row.querySelector(".nameInput").value;
            let updatedPosition = row.querySelector(".positionInput").value;

            if ( updatedTeamRole !== originalTeamRole || updatedPlayerNum !== originalPlayerNum ||
                                updatedName !== originalName || updatedPosition !== originalPosition) {
                console.log(updatedName)
                console.log(updatedPosition)
                console.log(originalPosition)
                let teamMemberUpdateDto = {
                    teamMemId: teamMemId,
                    teamRole: updatedTeamRole,
                    name: updatedName,
                    playerNum: updatedPlayerNum,
                    position: updatedPosition,
                };
                teamMemberUpdateDtos.push(teamMemberUpdateDto);};
        });

        var url = "/team-members"
        console.log("teamMemberUpdateDtos",teamMemberUpdateDtos.length);

        fetch(url,{
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(teamMemberUpdateDtos)
        })
        .then(response => response.json())
        .then(success => {
            alert("수정 완료되었습니다.")
            window.location.reload();
        })
        .catch(error => {
            console.error("Error:", error)
            alert("수정 중 오류 발생 ", error)
        });
    });
});
