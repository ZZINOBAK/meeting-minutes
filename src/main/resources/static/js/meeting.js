$(document).ready(function () {

    // --- 1. 초기 세팅 ---
    setupDateTimeInputs('meetingHour', 'meetingMin', 'meetingDay');

    // --- 2. 실시간 검증 (제목, 사번) ---
    $('#title').on('input', function () {
        const val = $(this).val();
        const $msg = $('#titleMsg');
        if (val.length < 2) {
            $msg.text('2글자 이상 입력하세요.').css('color', 'red');
        } else {
            $msg.text('확인되었습니다.').css('color', 'green');
        }
    });

    $('#empino').on('input', function () {
        const val = $(this).val();
        const regex = /^[0-9]{10}$/;
        $('#empinoMsg').text(regex.test(val) ? '확인됨' : '숫자 10자리 입력').css('color', regex.test(val) ? 'green' : 'red');
    });

    // --- 3. 파일 관련 ---
    $('input[name="file"]').on('change', function () {
        if (this.files[0] && !isValidFile(this.files[0])) $(this).val('');
    });

    // // --- 4. 모달 제어 ---
    // const $modal = $('#employeeModal');
    // $('#btnOpenModal').on('click', function () {
    //     // 1. 검색어 입력창 비우기
    //     $('#modalSearchInput').val('');

    //     // 2. 검색 결과 영역 비우고 안내 문구 넣기
    //     $('#modalSearchResult').html('<p style="color:#999; text-align:center;">이름을 검색해 주세요.</p>');

    //     // 3. (추가) 혹시 남아있을지 모를 체크박스들 초기화
    //     $('input[name="selectedEmp"]').prop('checked', false);

    //     $modal.show();
    //     $('#modalSearchInput').focus();
    // });

    // $('#btnCloseModal').on('click', () => $modal.hide());
    // $(window).on('click', (e) => { if ($(e.target).is($modal)) $modal.hide(); });

    // // 모달 내 검색 버튼
    // $('#btnModalSearch').on('click', function () {
    //     const keyword = $('#modalSearchInput').val().trim();
    //     performEmployeeSearch(keyword, $('#modalSearchResult'));
    // });

    // // --- 5. 참석자 태그 추가 (btnAddSelected) ---
    // $('#btnAddSelected').on('click', function () {
    //     const $checked = $('input[name="selectedEmp"]:checked');
    //     $checked.each(function () {
    //         const empino = $(this).val();
    //         const name = $(this).data('name');
    //         const dept = $(this).data('dept');

    //         if ($(`#attendeeList [data-id="${empino}"]`).length === 0) {
    //             $('.empty-msg').hide();
    //             const tag = `
    //                 <div class="attendee-tag" data-id="${empino}" style="display:inline-block; background:#eef; padding:5px; margin:5px; border-radius:5px;">
    //                     <span>${name} (${dept})</span>
    //                     <input type="hidden" name="attendeeIds" value="${empino}">
    //                     <span class="remove-btn" style="cursor:pointer; color:red; margin-left:5px;">&times;</span>
    //                 </div>`;
    //             $('#attendeeList').append(tag);
    //         }
    //     });
    //     $modal.hide();
    // });

    // 태그 삭제 이벤트 (위임 방식)
    $(document).on('click', '.remove-btn', function () {
        $(this).parent().remove();
        if ($('.attendee-tag').length === 0) $('.empty-msg').show();
    });

    // --- 6. 폼 최종 제출 ---
    $('#registerForm').on('submit', function (e) {
        combineDateTime('meetingDay', 'meetingHour', 'meetingMin', 'meetingDate');

        if ($('#title').val().length < 2 || !(/^[0-9]{10}$/.test($('#empino').val()))) {
            alert("입력 양식을 확인해 주세요.");
            e.preventDefault();
            return false;
        }
    });
});