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

        document.querySelectorAll(".fieldError").forEach(el => {
            el.classList.add("hidden");
            el.textContent = "";
        });
    });

//  수정 요청 -> PATCH 요청
    saveButton.addEventListener("click", function() {
        let teamMemberUpdateDtos = [];
        let teamId = saveButton.getAttribute("data-team-id");

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

        let requestBody = {
            teamId: teamId,
            teamMemberUpdateDtos: teamMemberUpdateDtos
        };

        fetch("/team-members",{
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(requestBody)
        })
        .then(response => {
            if(response.ok) {
                alert("수정 완료되었습니다.")
                window.location.reload();
            } else {
                return response.json()
            }
        })
        .then(errors => {
            if (errors) {
                displayErrors(errors)
            }
        })
        .catch(error => {
            console.error("Error:", error)
            alert("수정 중 오류 발생 ", error)
        });
    });
});

function displayErrors(errors) {
    // 기존 오류 메시지 제거
    document.querySelectorAll(".fieldError").forEach(errorElem => {
        errorElem.classList.add("hidden");
        errorElem.textContent = "";
    });

    Object.entries(errors).forEach(([teamMemId, fieldErrors]) => {
        const row = document.querySelector(`tr[data-teamMem-id='${teamMemId}']`);

        if (!row) return;

        Object.entries(fieldErrors).forEach(([field, message]) => {
            console.log("field",field)
            console.log("message",message)
            const errorElem = row.querySelector(`.${field}Error`);
            if (errorElem) {
                errorElem.textContent = message;
                errorElem.classList.remove("hidden");
            }
        });
    });
}
