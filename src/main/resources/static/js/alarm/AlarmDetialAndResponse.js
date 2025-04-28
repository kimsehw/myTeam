function openAlarmDetailModal(alarm) {
    const modal = document.getElementById("alarm_detail_modal");
    const content = document.getElementById("alarm_detail_content");
    content.innerText = alarm.detail || "상세 정보가 없습니다.";

    modal.classList.remove("hidden");

    // 버튼 동작 예시
    document.getElementById("alarm_accept_btn").onclick = () => {
        responseWithAction(alarm, true);
        modal.classList.add("hidden");
    };
    document.getElementById("alarm_reject_btn").onclick = () => {
        responseWithAction(alarm, false);
        modal.classList.add("hidden");
    };
    document.getElementById("alarm_close_btn").onclick = () => {
        modal.classList.add("hidden");
    };
}

function responseWithAction(alarm, response) {

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
    })
    .then(errors => {
        if (errors) {
            alert(errors.errorMessage)
        }
        togglePopup(true)
    })
    .catch(error => {
        console.error("Error:", error);
        alert('초대 응답 요청 중 오류가 발생했습니다.');
    });
}

