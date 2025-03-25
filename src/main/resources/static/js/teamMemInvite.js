document.addEventListener('DOMContentLoaded', function () {
    const teamId = document.getElementById("inviteForm").getAttribute("data-team-id");

    document.getElementById('inviteForm').addEventListener('submit', function (event) {
        event.preventDefault();

//        console.log("Fetching with teamId:", teamId);
        var playerNumError = document.getElementById("playerNumError");
        var emailError = document.getElementById("emailError");
        var nameError = document.getElementById("nameError");
        playerNumError.textContent = "";
        emailError.textContent = "";
        nameError.textContent = "";

        const formData = {
            email: document.getElementById('email').value,
            isNotUser: document.getElementById('isNotUser').checked === true,
            playerNum: document.getElementById('playerNum').value,
            name: document.getElementById('name').value
        };

//        console.log("formData: ", formData)

        fetch('/teams/' + teamId + '/team-members/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(formData)
        })
        .then(response => {
                if (response.ok) {
                    playerNumError.classList.toggle('hidden',true);
                    emailError.classList.toggle('hidden',true);
                    nameError.classList.toggle('hidden',true);

                    alert("초대가 성공적으로 전송되었습니다!");
                    window.location.href = '/teams/' + teamId+ '/team-members';
                }
                return response.json();
        })
        .then(errors => {
            console.log("errors: ",errors);
            if (!errors || Object.keys(errors).length === 0) return;

            if (errors.playerNum) {
                playerNumError.textContent = errors.playerNum;
                playerNumError.classList.toggle('hidden',false);
            }

            if (errors.email) {
                emailError.textContent = errors.email;
                emailError.classList.toggle('hidden',false);
            }

            if (errors.name) {
                nameError.textContent = errors.name;
                nameError.classList.toggle('hidden',false);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert('초대 요청 중 오류가 발생했습니다.');
        });

    });
});
