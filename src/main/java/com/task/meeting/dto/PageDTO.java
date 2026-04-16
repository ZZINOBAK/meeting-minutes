package com.task.meeting.dto;

import lombok.Getter;

@Getter
public class PageDTO {
    private int startPage;   // 시작 번호 (1, 11, 21...)
    private int endPage;     // 끝 번호 (10, 20, 30...)
    private boolean prev, next; // 이전, 다음 버튼 표시 여부
    private int total;       // 전체 게시글 수
    private Criteria cri;    // 현재 페이지 번호와 양(3개)

    public PageDTO(Criteria cri, int total) {
        this.cri = cri;
        this.total = total;

        // 끝 페이지 계산 (3개씩 보여줄 때 예: 현재 1페이지면 10, 현재 11페이지면 20)
        this.endPage = (int) (Math.ceil(cri.getPage() / 10.0)) * 10;
        this.startPage = this.endPage - 9;

        // 진짜 마지막 페이지 계산 (데이터가 10개뿐이면 끝 페이지는 10이 아니라 4여야 함 - 3개씩일 때)
        int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));

//        if (realEnd < this.endPage) {
//            this.endPage = realEnd;
//        }
//        if (this.endPage == 0) {
//            this.endPage = 1;
//        }

        // 2. endPage 보정: 공식으로 나온 endPage와 realEnd 중 더 작은 값을 선택
        this.endPage = Math.min(endPage, realEnd);

        // 3. 0 방지: 보정된 값과 1 중 더 큰 값을 선택 (최소 1 보장)
        this.endPage = Math.max(endPage, 1);

        this.prev = this.startPage > 1;
        this.next = this.endPage < realEnd;
    }
}
