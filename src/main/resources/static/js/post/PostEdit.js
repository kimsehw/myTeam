function deletePost(postId) {

    var url = `/posts/${postId}`

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
    .then(response => {
        alert("글이 삭제되었습니다.");
        window.location.href = '/posts';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('삭제 요청 중 오류가 발생했습니다.');
    });
}