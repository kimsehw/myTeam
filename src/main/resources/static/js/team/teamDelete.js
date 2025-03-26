function deleteTeam(teamId, logoUrl) {

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    //console.log('teamId:', teamId);
    //console.log('logoUrl:', logoUrl);

     var encodedLogoUrl = encodeURIComponent(logoUrl);
     var url = `/teams/${teamId}?logoUrl=${encodedLogoUrl}`;

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
                alert("팀이 삭제되었습니다.");
                window.location.href = '/';
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
}