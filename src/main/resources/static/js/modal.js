function toggleModal(modalId, show) {
    const modal = document.getElementById(modalId)
    if (!modal) {
        console.error(`모달 ID: ${modalId}를 찾을 수 없습니다.`);
        return;
    }
    // classList.toggle() 지정한 클래스 값을 추가 혹은 제거(두번째 boolean 으로 컨트롤)
    modal.classList.toggle('hidden',!show)
}

