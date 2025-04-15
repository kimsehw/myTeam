/*
function openTeamMemberModal(page = 0) {
    const listEl = document.getElementById("teamMemberList");
    listEl.innerHTML = ''; // 기존 내용 초기화

    currentPage = 0;
    isLastPage = false;
    isLoading = false;

    toggleModal('addTeamMemberModal', true);
    // 첫 페이지 조회
    loadNextPageAndFillUntilScrollable();

    // 스크롤 이벤트 등록
    listEl.onscroll = function () {
        console.log('스크롤');
        const scrollBottom = listEl.scrollTop + listEl.clientHeight;
        const scrollHeight = listEl.scrollHeight;

        if (!isLoading && !isLastPage && scrollBottom >= scrollHeight - 5) {
            loadNextPage();
        }
    };
    console.log('끝');
}

function loadNextPage() {
    isLoading = true;

    fetch(`/team-members/forAddMatch?page=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById("teamMemberList");
            data.content.forEach(teamMember => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <label class="flex items-center space-x-2">
                        <input type="checkbox" name="teamMemIds" value="${teamMember.id}" class="form-checkbox">
                        <span>등번호 ${teamMember.playerNum} - ${teamMember.name}</span>
                    </label>
                `;
                listEl.appendChild(li);
            });

            isLastPage = data.last;
            currentPage++;
            isLoading = false;
        });
}

function loadNextPageAndFillUntilScrollable() {
    isLoading = true;

    fetch(`/team-members/forAddMatch?page=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById("teamMemberList");

            data.content.forEach(teamMember => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <label class="flex items-center space-x-2">
                        <input type="checkbox" name="teamMemIds" value="${teamMember.id}" class="form-checkbox">
                        <span>등번호 ${teamMember.playerNum} - ${teamMember.name}</span>
                    </label>
                `;
                listEl.appendChild(li);
            });

            isLastPage = data.last;
            currentPage++;
            isLoading = false;

            // 💡 스크롤이 아직 생기지 않았고, 다음 페이지가 있다면 재귀 호출
            if (!isLastPage && listEl.scrollHeight <= listEl.clientHeight) {
                loadNextPageAndFillUntilScrollable();
            }
        });
}
*/
let selectedMatchId = null;

function openTeamMemberModal(matchId) {
    const listEl = document.getElementById("teamMemberList");
    listEl.innerHTML = ''; // 기존 내용 초기화

    selectedMatchId = matchId
    currentPage = 0;
    isLastPage = false;
    isLoading = false;

    toggleModal('addTeamMemberModal', true);

    loadNextPage(() => {
        listEl.onscroll = function () {
//          console.log('스크롤');
            const scrollBottom = listEl.scrollTop + listEl.clientHeight;
            const scrollHeight = listEl.scrollHeight;
            if (!isLoading && !isLastPage && scrollBottom >= scrollHeight - 5) {
                console.log("스크롤 바닥 도달 → 다음 페이지 로딩");
                loadNextPage();
            }
        };
    });
//    console.log('끝');
}

function loadNextPage(callback) {
    isLoading = true;

    fetch(`/team-members/forAddMatch?page=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById("teamMemberList");

            data.content.forEach(teamMember => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <label class="flex items-center space-x-2">
                        <input type="checkbox" name="addMemberIds" value="${teamMember.teamMemId}" class="form-checkbox">
                        <span>등번호 ${teamMember.playerNum} - ${teamMember.name}</span>
                    </label>
                `;
                listEl.appendChild(li);
            });

            isLastPage = data.last;
            currentPage++;
            isLoading = false;

            // 스크롤 안 생기면 계속 로드
            if (!isLastPage && listEl.scrollHeight <= listEl.clientHeight) {
                loadNextPage(callback);
            } else if (callback) {
                callback(); // 최초 로딩 후 스크롤 이벤트 등록용 콜백
            }
        });
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('addMemberSelectForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const selectedIds = Array.from(document.querySelectorAll('input[name="addMemberIds"]:checked'))
                    .map(checkbox => checkbox.value);
        const formData = {
            addMemberIds: selectedIds,
            matchId: selectedMatchId
        }

        var url = '/matches/addMember'

        fetch(url , {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              [csrfHeader]: csrfToken
            },
            body: JSON.stringify(formData)
        })
        .then(response => response.json().then(data => ({ status: response.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                alert("신청이 성공적으로 전송되었습니다!");
            } else {
                alert(body.errorMessage || "에러가 발생했습니다.");
            }
            window.location.reload();
        })
        .catch(error => {
             console.error("Error:", error);
             alert('참여인원 추가 요청 중 오류가 발생했습니다.');
        });
    });
});