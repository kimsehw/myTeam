document.addEventListener('DOMContentLoaded', function () {
    const teamId = document.getElementById("inviteForm").getAttribute("data-team-id");

    document.getElementById('inviteForm').addEventListener('submit', function (event) {
        event.preventDefault();

//        console.log("Fetching with teamId:", teamId);
        document.getElementById("playerNumError").textContent = ""
        document.getElementById("emailError").textContent = ""

        const formData = {
            email: document.getElementById('email').value,
            isNotUser: document.getElementById('isNotUser').checked === true,
            playerNum: document.getElementById('playerNum').value
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
                    var playerError = document.getElementById("playerError")
                    var emailError = document.getElementById("emailError")
                    if (playerError) {
                        playerError.classList.toggle('hidden',true)
                    }
                    if (emailError) {
                        emailError.classList.toggle('hidden',true)
                    }
                    alert("초대가 성공적으로 전송되었습니다!")
                    window.location.href = '/teams/' + teamId+ '/team-members'
                } else {
                    return response.json()
                }
            })
            .then(errors => {
//                console.log("errors: ",errors)
                if (errors.playerNum) {
                    document.getElementById("playerNumError").textContent = errors.playerNum
                    document.getElementById("playerNumError").classList.toggle('hidden',false)
                }

                if (errors.email) {
                    document.getElementById("emailError").textContent = errors.email
                    document.getElementById("emailError").classList.toggle('hidden',false)
                }
            })
            .catch(error => {
                console.error("Error:", error)
                alert('초대 요청 중 오류가 발생했습니다.');
            });

    });
});
