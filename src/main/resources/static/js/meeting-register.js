$(document).ready(function() {
    // --- 1. 회의 제목 실시간 검증 ---
    $('#title').on('input', function() {
        const val = $(this).val();
        const $msg = $('#titleMsg');

        if (val.length === 0) {
            $msg.text('필수 항목입니다.').addClass('msg-error').removeClass('msg-success');
            $(this).addClass('error-border').removeClass('success-border');
        } else if (val.length < 2) {
            $msg.text('두 글자 이상 입력해 주세요.').addClass('msg-error').removeClass('msg-success');
            $(this).addClass('error-border').removeClass('success-border');
        } else {
            $msg.text('사용 가능한 제목입니다.').addClass('msg-success').removeClass('msg-error');
            $(this).addClass('success-border').removeClass('error-border');
        }
    });

    // --- 2. 사번 실시간 검증 (숫자 10자리) ---
    $('#empino').on('input', function() {
        const val = $(this).val();
        const $msg = $('#empinoMsg');
        const regex = /^[0-9]{10}$/;

        if (val.length === 0) {
            $msg.text('필수 항목입니다.').addClass('msg-error').removeClass('msg-success');
        } else if (!regex.test(val)) {
            $msg.text('숫자 10자리를 정확히 입력해 주세요.').addClass('msg-error').removeClass('msg-success');
        } else {
            $msg.text('확인되었습니다.').addClass('msg-success').removeClass('msg-error');
        }
    });

    // --- 3. 폼 제출 시 최종 확인 ---
    $('#registerForm').on('submit', function(e) {
        const title = $('#title').val();
        const empino = $('#empino').val();
        const regex = /^[0-9]{10}$/;

        // 제목 검증
        if (title.length < 2) {
            $('#title').focus();
            return false; // 전송 중단
        }

        // 사번 검증
        if (!regex.test(empino)) {
            $('#empino').focus();
            return false; // 전송 중단
        }

        // 모든 검증 통과 시 전송 시작
    });

    const $hourSelect = $('#meetingHour');
    const $minSelect = $('#meetingMin');

    // 1. '시' 옵션 생성 (00~23)
    for (let h = 0; h < 24; h++) {
        const hh = String(h).padStart(2, '0');
        $hourSelect.append(`<option value="${hh}">${hh}</option>`);
    }

    // 2. 현재 시간 기준으로 초기값 세팅
    const now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();

    // 분 보정 (00 또는 30)
    if (minutes < 15) {
        minutes = "00";
    } else if (minutes < 45) {
        minutes = "30";
    } else {
        minutes = "00";
        hours = (hours + 1) % 24;
    }

    $('#meetingDay').val(now.toISOString().split('T')[0]).attr('min', now.toISOString().split('T')[0]);
    $hourSelect.val(String(hours).padStart(2, '0'));
    $minSelect.val(minutes);

    // 3. 전송 전 데이터 합치기
    $('#registerForm').on('submit', function() {
        const day = $('#meetingDay').val();
        const hour = $hourSelect.val();
        const min = $minSelect.val();
        $('#meetingDate').val(`${day}T${hour}:${min}`);
    });
});

$(document).ready(function() {
    // 파일 선택창(input[type="file"])이 변경될 때 실행
    $('input[name="file"]').on('change', function() {
        const file = this.files[0]; // 선택된 파일 가져오기
        
        if (file) {
            const maxSize = 10 * 1024 * 1024; // 10MB를 바이트 단위로 계산
            
            if (file.size > maxSize) {
                alert("파일 용량이 10MB를 초과합니다. 다시 선택해 주세요.");
                
                // [핵심] 선택된 파일 비우기
                $(this).val(''); 
                
                // 파일명 보여주는 영역이 따로 있다면 그것도 비워주세요
                // $('#fileNameDisplay').text('선택된 파일 없음');
            } else {
                // 용량이 통과되었다면 파일명을 콘솔이나 UI에 출력해볼 수 있어요
                console.log("선택된 파일: " + file.name + " (" + file.size + " bytes)");
            }
        }
    });
});

$(document).ready(function () {
    // 폼 제출 시 자바스크립트로 1차 유효성 검사 (과제 배점 포인트)
    $('#registerForm').on('submit', function (e) {
        const fileInput = $('#meetingFile')[0];

        if (fileInput.files.length > 0) {
            const file = fileInput.files[0];

            // 1. 파일 형식 체크
            if (file.type !== 'application/pdf') {
                alert('PDF 파일만 업로드 가능합니다.');
                return false;
            }

            // 2. 용량 체크 (10MB)
            // const maxSize = 10 * 1024 * 1024;
            const maxSize = 10 * 1024 * 1024;
            if (file.size > maxSize) {
                alert('파일 용량은 10MB를 초과할 수 없습니다.');
                return false;
            }
        }
    });
});

$(document).ready(function() {
    const $modal = $('#employeeModal');

    // [1] 모달 열기 버튼 클릭 시
    $('#btnOpenModal').on('click', function() {
        $modal.show(); // 모달 나타남
        $('#modalSearchInput').val('').focus(); // 검색창 초기화 및 포커스
        $('#modalSearchResult').html('<p style="color:#999; text-align:center;">이름을 검색해 주세요.</p>');
    });

    // [2] 모달 닫기 (X 버튼 클릭 시)
    $('#btnCloseModal').on('click', function() {
        $modal.hide();
    });

    // [3] 모달 바깥쪽 어두운 배경 클릭 시 닫기 (센스!)
    $(window).on('click', function(event) {
        if ($(event.target).is($modal)) {
            $modal.hide();
        }
    });

    // [4] 검색 버튼 클릭 시 (REST API 호출 준비)
    $('#btnModalSearch').on('click', function() {
        const keyword = $('#modalSearchInput').val().trim();
        if (keyword === "") {
            alert("검색어를 입력해 주세요.");
            return;
        }

        const resultArea = $('#modalSearchResult');
        resultArea.html('<p style="text-align:center;">검색 중...</p>');

        $.ajax({
            url: '/api/employees/search',
            type: 'GET',
            data: { keyword: keyword },
            success: function(data) {
                if (data.length === 0) {
                    resultArea.html('<p style="text-align:center; color:red;">검색 결과가 없습니다.</p>');
                    return;
                }

                let html = '<ul style="list-style:none; padding:0;">';
                data.forEach(emp => {
                    html += `
                        <li style="padding:10px; border-bottom:1px solid #eee;">
                            <label style="display:flex; align-items:center; cursor:pointer;">
                                <input type="checkbox" name="selectedEmp" value="${emp.empino}"
                                       data-name="${emp.empinm}" data-dept="${emp.dept}"
                                       style="margin-right:10px;">
                                <span>
                                    <strong>${emp.empinm}</strong>
                                    <small style="color:#666;">(${emp.dept} / ${emp.position})</small>
                                </span>
                            </label>
                        </li>`;
                });
                html += '</ul>';
                resultArea.html(html);
            },
            error: function() {
                resultArea.html('<p style="text-align:center; color:red;">에러가 발생했습니다.</p>');
            }
        });
    });
});
