document.addEventListener("DOMContentLoaded", function() {
    let currentDate = new Date();
    const calendarMonth = document.getElementById("calendarMonth");
    const calendarDays = document.getElementById("calendarDays");

    // 예제 일정 데이터 (서버에서 가져올 예정)
    const schedules = [
        { date: "2025-03-10", type: "practice", name: "연습 경기" },
        { date: "2025-03-17", type: "league", name: "리그 경기" }
    ];

    // 날짜를 LocalDateTime 데이터와 비교하여 일정이 있으면 표시
    function renderCalendar(date) {
        calendarDays.innerHTML = ""; // 기존 달력 초기화
        const year = date.getFullYear();
        const month = date.getMonth();

        // 월 표기 (ex: 2024년 3월)
        calendarMonth.textContent = `${year}년 ${month + 1}월`;

        // 해당 월의 첫날과 마지막 날 계산
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const firstWeekday = firstDay.getDay();
        let calendarBoxClass = "relative bg-white p-2 h-16 text-sm";

        // 이전 달 공백 채우기
        for (let i = 0; i < firstWeekday; i++) {
            calendarDays.innerHTML += `<div class="${calendarBoxClass}"></div>`;
        }

        // 현재 달 날짜 채우기
        for (let day = 1; day <= lastDay.getDate(); day++) {
            let fullDate = `${year}-${(month + 1).toString().padStart(2, "0")}-${day.toString().padStart(2, "0")}`;
            let schedule = schedules.find(s => s.date === fullDate);
            let scheduleHtml = "";

            if (schedule) {
                let colorClass = schedule.type === "league" ? "bg-red-100 text-red-800" : "bg-blue-100 text-blue-800";
                scheduleHtml = `<div class="mt-1 text-xs p-1 rounded ${colorClass}">${schedule.name}</div>`;
            }

            calendarDays.innerHTML += `
                <div class="${calendarBoxClass}">
                    ${day}
                    ${scheduleHtml}
                </div>`;
        }
    }

    // 이전 달 이동
    document.getElementById("prevMonth").addEventListener("click", function() {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar(currentDate);
    });

    // 다음 달 이동
    document.getElementById("nextMonth").addEventListener("click", function() {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar(currentDate);
    });

    // 초기 달력 렌더링
    renderCalendar(currentDate);
});
