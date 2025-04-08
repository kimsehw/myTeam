document.addEventListener("DOMContentLoaded", function () {
    const searchDateType = document.getElementById("searchDateType");
    const fromDate = document.getElementById("fromDate");
    const toDate = document.getElementById("toDate");

    function handleDateChange() {
        if (fromDate.value || toDate.value) {
            searchDateType.value = ""; // 기간 선택 초기화
            searchDateType.setAttribute("disabled", "true"); // 비활성화
        } else {
            searchDateType.removeAttribute("disabled"); // 활성화
        }
    }

    function handlePeriodChange() {
        if (searchDateType.value) {
            fromDate.value = "";
            toDate.value = "";
            fromDate.setAttribute("disabled", "true"); // 비활성화
            toDate.setAttribute("disabled", "true"); // 비활성화
        } else {
            fromDate.removeAttribute("disabled"); // 활성화
            toDate.removeAttribute("disabled"); // 활성화
        }
    }

    fromDate.addEventListener("change", handleDateChange);
    toDate.addEventListener("change", handleDateChange);
    searchDateType.addEventListener("change", handlePeriodChange);
});