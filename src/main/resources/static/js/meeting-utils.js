/**
 * 회의 등록 관련 공통 함수 모음 (도구상자)
 */

// [1] 날짜/시간 초기 세팅 로직
function setupDateTimeInputs(hourId, minId, dayId) {
    const $hourSelect = $(`#${hourId}`);
    for (let h = 0; h < 24; h++) {
        const hh = String(h).padStart(2, '0');
        $hourSelect.append(`<option value="${hh}">${hh}</option>`);
    }

    const now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes() < 15 ? "00" : (now.getMinutes() < 45 ? "30" : "00");
    if (now.getMinutes() >= 45) hours = (hours + 1) % 24;

    $(`#${dayId}`).val(now.toISOString().split('T')[0]).attr('min', now.toISOString().split('T')[0]);
    $hourSelect.val(String(hours).padStart(2, '0'));
    $(`#${minId}`).val(minutes);
}

// [2] 날짜/시간 데이터 병합 (서버 전송용)
function combineDateTime(dayId, hourId, minId, targetId) {
    const day = $(`#${dayId}`).val();
    const hour = $(`#${hourId}`).val();
    const min = $(`#${minId}`).val();
    $(`#${targetId}`).val(`${day}T${hour}:${min}`);
}


// [4] 파일 유효성 검사
function isValidFile(file) {
    const maxSize = 10 * 1024 * 1024;
    if (file.type !== 'application/pdf') {
        alert('PDF 파일만 가능합니다.');
        return false;
    }
    if (file.size > maxSize) {
        alert('10MB를 초과할 수 없습니다.');
        return false;
    }
    return true;
}