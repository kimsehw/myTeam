document.addEventListener('DOMContentLoaded', function () {
    const teamId = document.getElementById("inviteForm").getAttribute("data-team-id");
    document.getElementById('inviteForm').addEventListener('submit', function (event) {
        event.preventDefault();

        var inviteeError = document.getElementById("inviteeError");
        var inviteeTeamNameError = document.getElementById("inviteeTeamNameError");
        var matchDateError = document.getElementById("matchDateError");
        var matchTimeError = document.getElementById("matchTimeError");
        inviteeError.textContent = "";
        inviteeTeamNameError.textContent = "";
        matchDateError.textContent = "";
        matchTimeError.textContent = "";

        const formData = {
            isNotUser: document.getElementById('isNotUser').checked,
            inviteeTeamId: document.getElementById('inviteeTeamId').value,
            inviteeEmail: document.getElementById('inviteeEmail').value,
            inviteeTeamName: isNotUser
                                     ? document.getElementById('notUserTeamNameInput').value
                                     : document.getElementById('inviteeTeamName').value,
            matchDate: document.getElementById('matchDate').value,
            matchTime: document.getElementById('matchTime').value
        };

        var url = '/matches/invite'

        fetch(url , {
         method: 'POST',
         headers: {
             'Content-Type': 'application/json',
             [csrfHeader]: csrfToken
         },
         body: JSON.stringify(formData)
        })
        .then(response => {
            if (response.ok) {
                inviteeError.classList.toggle('hidden',true);
                inviteeTeamNameError.classList.toggle('hidden',true);
                matchDateError.classList.toggle('hidden',true);
                matchTimeError.classList.toggle('hidden',true);

                alert("신청이 성공적으로 전송되었습니다!");
                window.location.href = '/teams/' + teamId+ '/matches';
            }
            return response.json();
        })
        .then(errors => {
            console.log("errors: ",errors);
            if (!errors || Object.keys(errors).length === 0) return;
            if (errors.invitee) {
                inviteeError.textContent = errors.invitee;
                inviteeError.classList.toggle('hidden',false);
            }
            if (errors.inviteeTeamName) {
                inviteeTeamNameError.textContent = errors.inviteeTeamName;
                inviteeTeamNameError.classList.toggle('hidden',false);
            }
            if (errors.matchDate) {
                matchDateError.textContent = errors.matchDate;
                matchDateError.classList.toggle('hidden',false);
            }
            if (errors.matchTime) {
                matchTimeError.textContent = errors.matchTime;
                matchTimeError.classList.toggle('hidden',false);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert('매치 신청 요청 중 오류가 발생했습니다.');
        });

    });
});