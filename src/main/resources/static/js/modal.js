function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId)
    if (!modal) {
        console.error(`모달 ID: ${modalId}를 찾을 수 없습니다.`);
        return;
    }
    // classList.toggle() 지정한 클래스 값을 추가 혹은 제거(두번째 boolean 으로 컨트롤)
    modal.classList.toggle('hidden',!show)
}

function openTeamMemDeleteModal(button, modalName) {
    // 버튼에서 데이터 속성 가져오기
    let teamMemId = button.getAttribute("data-teamMem-id");
    let name = button.getAttribute("data-teamMem-name");
    let teamId = button.getAttribute("data-team-id");

    // 모달 내부의 버튼에 해당 ID 값 설정
    console.log(name)
    console.log(teamMemId)
    console.log(teamId)
    document.getElementById("deleteBtn").setAttribute("onclick", `deleteTeamMem(${teamMemId}, '${name}',${teamId})`);
    document.getElementById("askName").textContent = '정말 '+name+' 선수를 방출하시겠습니까?';

    // 모달을 표시
    toggleModal(modalName, true);
}