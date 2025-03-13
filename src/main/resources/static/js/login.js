const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault(); // 폼 기본 제출 막기

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch('/members/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: new URLSearchParams({ email, password })
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/'; // 로그인 성공 → 홈 이동
        } else {
            return response.json(); // 실패 → JSON 에러 메시지 받기
        }
    })
    .then(data => {
        if (data && data.error) {
            alert(data.error); // 모달 내에 에러 메시지 출력
            showError(data.error); // 모달 내에 에러 메시지 출력
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그인 요청 중 오류가 발생했습니다.');
    });
});

// 에러 메시지 표시 함수
function showError(message) {
    const errorMsg = document.getElementById('loginErrorMsg');
    errorMsg.textContent = message;
    errorMsg.classList.remove('hidden'); // 에러 메시지 표시
}
