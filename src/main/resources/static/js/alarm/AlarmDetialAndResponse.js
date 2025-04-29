function openAlarmDetailModal(alarm, liEl) {
    const modal = document.getElementById("alarm_detail_modal");
    const content = document.getElementById("alarm_detail_content");
    const acceptBtn = document.getElementById("alarm_accept_btn");
    const rejectBtn = document.getElementById("alarm_reject_btn");

    content.innerText = alarm.detail || "상세 정보가 없습니다.";

    // 버튼 표시 여부
    if (["TEAM_INVITE", "MATCH_INVITE"].includes(alarm.alarmType) && !alarm.isSent) {
        acceptBtn.style.display = 'inline-block';
        rejectBtn.style.display = 'inline-block';
    } else {
        acceptBtn.style.display = 'none';
        rejectBtn.style.display = 'none';
    }

    readAndOpenModal(alarm,modal,liEl)

    acceptBtn.onclick = () => {
            responseWithAction(alarm, true, liEl);
            modal.classList.add("hidden");
    };
    rejectBtn.onclick = () => {
        responseWithAction(alarm, false, liEl);
        modal.classList.add("hidden");
    };
    document.getElementById("alarm_close_btn").onclick = () => {
        modal.classList.add("hidden");
    };
}

function responseWithAction(alarm, response, liEl) {

    const responseFromDto = {
        inviteAlarmId: alarm.id,
        fromMemId: alarm.toMemId,
        toMemId: alarm.fromMemId,
        fromTeamId: alarm.toTeamId,
        toTeamId: alarm.fromTeamId,
        alarmType: alarm.alarmType,
        response: response
    };

    fetch('/alarms/responseWithAction', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(responseFromDto)
    })
    .then(response => {
        if (!response.ok) {
            return response.json()
        }
        alert("초대 응답 완료하였습니다.")
        liEl.remove();
    })
    .then(errors => {
        if (errors) {
            alert(errors.errorMessage)
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert('초대 응답 요청 중 오류가 발생했습니다.');
    });
}

function readAndOpenModal(alarm, modal,liEl) {

    fetch(`/alarms/${alarm.id}/read`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json()
        }
        liEl.classList.add("bg-gray-100");
        modal.classList.remove("hidden");
    })
    .then(errors => {
        if (errors) {
            alert(errors.errorMessage)
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert('응답 세부 내용 요청 중 오류가 발생했습니다.');
    });
}

