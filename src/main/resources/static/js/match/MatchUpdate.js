document.addEventListener("DOMContentLoaded", () => {
    const menu = document.getElementById("floatingMenu");
    let selectedRow = null;
    let isEditing = false;

    document.querySelectorAll(".match-row").forEach(row => {
        // 저장용 초기값 미리 세팅
        row.dataset.originalStadium = row.querySelector(".stadiumInput")?.value || '';
        row.dataset.originalDate = row.querySelector(".matchDateInput")?.value || '';
        row.dataset.originalTeamName = row.querySelector(".teamNameInput")?.value || '';
        row.dataset.originalRegion = row.querySelector(".regionInput")?.value || '';
        row.dataset.originalAge = row.querySelectorAll(".regionInput")[1]?.value || '';

        row.addEventListener("click", (event) => {
            if (isEditing) return;

            event.stopPropagation(); // prevent global close
            selectedRow = row;

            menu.style.left = `${event.pageX}px`;
            menu.style.top = `${event.pageY}px`;
            menu.classList.remove("hidden");
        });
    });

    document.addEventListener("click", () => {
        menu.classList.add("hidden");
    });

    document.querySelector(".edit-btn").addEventListener("click", () => {
        if (!selectedRow) return;
        isEditing = true;
        selectedRow.querySelectorAll(".view-mode").forEach(el => el.classList.add("hidden"));
        selectedRow.querySelectorAll(".edit-mode").forEach(el => el.classList.remove("hidden"));
        selectedRow.querySelector(".save-btn").classList.remove("hidden");
        selectedRow.querySelector(".cancel-btn").classList.remove("hidden");
        selectedRow.querySelector(".save-td").classList.remove("hidden");

        menu.classList.add("hidden");
    });

    document.querySelector(".detail-btn").addEventListener("click", () => {
        if (!selectedRow) return;
        const matchId = selectedRow.getAttribute("data-match-id");
        window.location.href = `/matches/${matchId}`;
    });

    document.querySelector(".record-btn").addEventListener("click", () => {
        if (!selectedRow) return;
        const matchId = selectedRow.getAttribute("data-match-id");
        window.location.href = `/matches/${matchId}/record`;
    });

    // ✅ 저장 버튼
    document.querySelectorAll(".save-btn").forEach(saveBtn => {
        saveBtn.addEventListener("click", () => {
            if (!selectedRow) return;
            const matchId = selectedRow.getAttribute("data-match-id");

            const stadium = selectedRow.querySelector(".stadiumInput").value;
            let matchDate = selectedRow.querySelector(".matchDateInput").value;
            function formatToDateTimeLocal(dateStr) {
              const [datePart, timePart] = dateStr.split(' ');
              const [year, month, day] = datePart.split('.');
              const [hour, minute] = timePart.split(':');

              return `${year}-${month}-${day}T${hour}:${minute}`;
            }
            if(matchDate == '') {
                matchDate = formatToDateTimeLocal(selectedRow.getAttribute("data-match-matchDate"));
            }
            const matchTime = selectedRow.getAttribute("data-match-matchTime");
            const teamName = selectedRow.querySelector(".teamNameInput").value;
            const region = selectedRow.querySelector(".regionInput").value;
            const ageRange = selectedRow.querySelectorAll(".regionInput")[1].value;

            const payload = {
                id: matchId,
                stadium: stadium,
                matchDate: matchDate,
                matchTime: matchTime,
                isNotUserMatch: true,
                opposingTeamName: teamName,
                opposingTeamRegion: region,
                opposingTeamAgeRange: ageRange
            };

            fetch(`/matches/update`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(payload)
            })
            .then(response => {
                if (response.ok) {
                    alert("수정 완료!");
                    window.location.reload();
                } else {
                    return response.json()
                }
            })
            .then(errors => {
                if (errors) {
                    console.log(errors)
                    alert("수정 실패")
                }
            })
        });
    });

    // ✅ 취소 버튼
    document.querySelectorAll(".cancel-btn").forEach(cancelBtn => {
        cancelBtn.addEventListener("click", () => {
            if (!selectedRow) return;

            // input/select 초기화
            selectedRow.querySelector(".stadiumInput").value = selectedRow.dataset.originalStadium;
            selectedRow.querySelector(".matchDateInput").value = selectedRow.dataset.originalDate;
            selectedRow.querySelector(".teamNameInput").value = selectedRow.dataset.originalTeamName;
            selectedRow.querySelector(".regionInput").value = selectedRow.dataset.originalRegion;
            selectedRow.querySelectorAll(".regionInput")[1].value = selectedRow.dataset.originalAge;

            // 모드 전환
            selectedRow.querySelectorAll(".view-mode").forEach(el => el.classList.remove("hidden"));
            selectedRow.querySelectorAll(".edit-mode").forEach(el => el.classList.add("hidden"));
            selectedRow.querySelector(".save-btn").classList.add("hidden");
            selectedRow.querySelector(".cancel-btn").classList.add("hidden");
            selectedRow.querySelector(".save-td")?.classList.add("hidden");

            isEditing = false;
        });
    });
});
