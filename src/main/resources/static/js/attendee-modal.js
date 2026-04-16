$(document).ready(function () {
    const $modal = $('#employeeModal');

    // 1. 모달 열기
    $('#btnOpenModal').on('click', function () {
        $('#modalSearchInput').val('');
        $('#modalSearchResult').html('<p style="color:#999; text-align:center;">이름을 검색해 주세요.</p>');
        $('input[name="selectedEmp"]').prop('checked', false);
        $modal.show();
        $('#modalSearchInput').focus();
    });

    // 2. 모달 닫기
    $('#btnCloseModal').on('click', () => $modal.hide());
    $(window).on('click', (e) => { if ($(e.target).is($modal)) $modal.hide(); });

    // 3. 검색 버튼 클릭
    $('#btnModalSearch').on('click', function () {
        const keyword = $('#modalSearchInput').val().trim();
        // performEmployeeSearch 함수가 meeting-util.js에 있다면 이 파일도 같이 불러줘야 함
        if (typeof performEmployeeSearch === 'function') {
            performEmployeeSearch(keyword, $('#modalSearchResult'));
        }
    });

    // 4. 엔터키 검색 허용
    $('#modalSearchInput').on('keyup', function (e) {
        if (e.key === 'Enter') $('#btnModalSearch').click();
    });

    $('#btnAddSelected').on('click', function () {
        const $checked = $('input[name="selectedEmp"]:checked');

        // 현재 페이지에 존재하는 참석자 리스트 영역을 찾습니다.
        // 등록 페이지라면 #attendeeList, 수정 모드라면 #editAttendeeList
        const $targetList = $('#editAttendeeList').length > 0 ? $('#editAttendeeList') : $('#attendeeList');
        const $emptyMsg = $('.empty-msg');

        $checked.each(function () {
            const empino = $(this).val();
            const name = $(this).data('name');
            const dept = $(this).data('dept');
            const position = $(this).data('position') || ''; // 직급 데이터가 있다면 추가

            // 1. 중복 체크 (현재 타겟 리스트 내에 이미 있는지 확인)
            if ($targetList.find(`[data-id="${empino}"]`).length === 0) {
                $emptyMsg.hide();

                // 2. 태그 생성
                const tag = `
            <div class="attendee-tag" data-id="${empino}">
                <span>${name} (${dept}/${position})</span>
                <input type="hidden" name="attendeeIds" value="${empino}">
                <span class="remove-btn" onclick="$(this).parent().remove()">&times;</span>
            </div>`;

                $targetList.append(tag);
            }
        });

        $('#employeeModal').hide();
    });
});

// [3] 사원 검색 AJAX
function performEmployeeSearch(keyword, $resultArea) {
    if (!keyword) return alert("검색어를 입력해 주세요.");
    $resultArea.html('<p style="text-align:center;">검색 중...</p>');

    $.ajax({
        url: '/api/employee/search',
        type: 'GET',
        data: { keyword: keyword },
        success: function (data) {
            if (data.length === 0) {
                $resultArea.html('<p style="text-align:center; color:red;">결과가 없습니다.</p>');
                return;
            }
            let html = '<ul style="list-style:none; padding:0;">';
            data.forEach(emp => {
                html += `
                    <li style="padding:10px; border-bottom:1px solid #eee;">
                        <label style="display:flex; align-items:center; cursor:pointer;">
                            <input type="checkbox" name="selectedEmp" value="${emp.empino}"
                                   data-name="${emp.empinm}" data-dept="${emp.dept}" data-position="${emp.position}">
                            <span style="margin-left:10px;">
                                <strong>${emp.empinm}</strong> (${emp.dept} / ${emp.position})
                            </span>
                        </label>
                    </li>`;
            });
            $resultArea.html(html + '</ul>');
        },
        error: () => $resultArea.html('<p style="text-align:center; color:red;">에러 발생</p>')
    });
}
