function loadNextAlarms(callback, isSent = null, isRead = null, alarmType = null) {
    isLoading = true;

    const params = new URLSearchParams();
    params.append("page", currentPage);
    if (isRead != null) params.append("isRead", isRead);
    if (isSent != null) params.append("isSent", isSent);
    if (alarmType !== null) params.append("alarmType", alarmType);

    fetch(`/alarms?${params.toString()}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        const listEl = document.getElementById("alarmList");

        data.content.forEach(alarm => {
            console.log(alarm)
            const li = document.createElement("li");
            li.innerHTML = `
                <label class="flex items-center space-x-2">
                    <span class="bg-green-500 text-white text-xs font-bold px-2 py-1 rounded-lg hover:bg-green-600 w-[75px] text-center">
                        ${alarm.alarmType}
                    </span>
                    <a class="text-gray-800 text-xs leading-snug block line-clamp-2 w-[300px]">
                        ${alarm.summary}
                    </a>
                </label>
            `;
            listEl.appendChild(li);
        });

        isLastPage = data.last;
        currentPage++;
        isLoading = false;
        // 스크롤 안 생기면 계속 로드
        if (!isLastPage && listEl.scrollHeight <= listEl.clientHeight) {
            loadNextAlarms(callback, isSent, isRead, alarmType);
        } else if (callback) {
            callback(); // 최초 로딩 후 스크롤 이벤트 등록용 콜백
        }
    });
}

function openAlarmPopup() {
    const listEl = document.getElementById("alarmList");
    listEl.innerHTML = ''; // 기존 내용 초기화

    currentPage = 0;
    isLastPage = false;
    isLoading = false;

    togglePopup();

    loadNextAlarms(() => {
        listEl.onscroll = function () {
//          console.log('스크롤');
            const scrollBottom = listEl.scrollTop + listEl.clientHeight;
            const scrollHeight = listEl.scrollHeight;
            if (!isLoading && !isLastPage && scrollBottom >= scrollHeight - 5) {
//                console.log("스크롤 바닥 도달 → 다음 페이지 로딩");
                loadNextAlarms(null, null, null, "ALARM");
            }
        };
    }, null, null, "ALARM");
//    console.log('끝');
}

function filterAlarmPopup(isSent = null, isRead = null, alarmType = null, btnEl = null) {
//    console.log("필터 파라미터 확인:", { isSent, isRead, alarmType });
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('bg-blue-100', 'text-blue-700');
    });
    if (btnEl) {
        btnEl.classList.add('bg-blue-100', 'text-blue-700');
    }

    const listEl = document.getElementById("alarmList");
    listEl.innerHTML = ''; // 기존 내용 초기화

    currentPage = 0;
    isLastPage = false;
    isLoading = false;

    loadNextAlarms(() => {
        listEl.onscroll = function () {
//          console.log('스크롤');
            const scrollBottom = listEl.scrollTop + listEl.clientHeight;
            const scrollHeight = listEl.scrollHeight;
            if (!isLoading && !isLastPage && scrollBottom >= scrollHeight - 5) {
//                console.log("스크롤 바닥 도달 → 다음 페이지 로딩");
                loadNextAlarms(null, isSent, isRead, alarmType);
            }
        };
    }, isSent, isRead, alarmType);
//    console.log('끝');
}

function togglePopup() {
    const alarms_popup = document.getElementById('alarms_popup');
    const alarms_btn = document.getElementById('alarms_btn');

     const expanded = alarms_btn.getAttribute('aria-expanded') === 'true';

    if (expanded) {
        alarms_popup.classList.add('hidden');
        alarms_btn.setAttribute('aria-expanded', 'false');
    } else {
        alarms_popup.classList.remove('hidden');
        alarms_btn.setAttribute('aria-expanded', 'true');

        const btnRect = alarms_btn.getBoundingClientRect();
        alarms_popup.style.top = `${btnRect.top + window.scrollY + 30}px`;
        alarms_popup.style.left = `${btnRect.left + window.scrollX - 350}px`; // 종 왼쪽으로 350px 이동
    }
}