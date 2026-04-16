package com.task.meeting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
    private int page;     // 현재 페이지 번호
    private int amount;   // 한 페이지당 데이터 개수 (예: 10개)
    private String type;    // 검색 타입 (T, C, TC)
    private String keyword; // 검색어

    public Criteria() {
        this(1, 10); // 기본값: 1페이지, 10개씩
    }

    public Criteria(int page, int amount) {
        this.page = page;
        this.amount = amount;
    }

    // 마이바티스 LIMIT 구문에서 사용할 시작 위치 계산
    public int getSkip() {
        return (this.page - 1) * this.amount;
    }

    public String[] getTypeArr() {
        return type == null ? new String[] {} : type.split("");
    }
}
