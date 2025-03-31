function deleteTeamMem(teamMemId,name,teamId) {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    var url = `/team-members/${teamMemId}`

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken,
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => {
        if (response.ok) {
            alert(`${name}` + ' 선수를 방출했습니다.');
            window.location.href = '/teams/' + teamId+ '/team-members';
        } else {
            return response.text();
        }
    })
    .then(errorMessage => {
        if (errorMessage) {
            alert("삭제 실패: " + errorMessage);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('삭제 요청 중 오류가 발생했습니다.');
    });
};