function bindDomEvent() {
    $("#teamLogoFile").on("change", function() {
        var fileInput = $(this)[0];
        var file = fileInput.files[0]; // 선택된 파일
        var fileName = file ? file.name : "";  // 파일명이 있으면 저장, 없으면 빈 문자열
        var fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); // 확장자 추출 및 소문자로 변환

        // 파일명 표시할 요소 가져오기
        var fileNameDisplay = $("#fileNameDisplay");

        // 이미지 확장자 검증
        if (file && (fileExt !== "jpg" && fileExt !== "jpeg" && fileExt !== "gif" && fileExt !== "png" && fileExt !== "bmp")) {
            alert("이미지 파일만 등록이 가능합니다.");
            $(this).val(""); // 입력 필드 초기화
            fileNameDisplay.text(""); // 파일명 초기화
            $("#imagePreview").attr("src", "").hide(); // 미리 보기 숨김
            return;
        }

        // 이미지 미리보기 처리
        if (file) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $("#imagePreview").attr("src", e.target.result).show(); // 이미지 태그 src 설정 및 표시
            };
            reader.readAsDataURL(file);
        }
    });
}

// 문서가 로드되면 이벤트 바인딩
$(document).ready(function() {
    bindDomEvent();
});
