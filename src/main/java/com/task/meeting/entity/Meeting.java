package com.task.meeting.entity;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Meeting {
    // 회의록 ID (PK)
    private Long meetingId;

    // 제목: 필수, 2~100자
    @NotBlank(message = "회의 제목은 필수입니다.")
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이내여야 합니다.")
    private String title;

    // 회의 일시: 필수, 미래 날짜만 (@Future)
    @NotNull(message = "회의 일시는 필수입니다.")
    @Future(message = "회의 일시는 현재보다 미래여야 합니다.")
    private LocalDateTime meetingDate;

    // 장소: 선택, 최대 50자
    @Size(max = 50, message = "장소는 50자 이내여야 합니다.")
    private String location;

    // 작성자 사번: 숫자 전용, 정확히 10자 (@Pattern 사용)
    @NotBlank(message = "사번은 필수입니다.")
    @Pattern(regexp = "^[0-9]{10}$", message = "사번은 숫자 10자리여야 합니다.")
    private String empino;

    // 작성자 이름: 선택, 한글/영문, 2~10자
    @Pattern(regexp = "^$|^[a-zA-Z가-힣]{2,10}$", message = "이름은 한글 또는 영문 2~10자여야 합니다.")
    private String empinm;

    // 내용: 선택, 최대 4000자
    @Size(max = 4000, message = "내용은 4000자 이내여야 합니다.")
    private String content;

    private LocalDateTime deleteAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 첨부파일 추가
    private Attachment attachment;
    // 참석자 조회, 출력
    private List<Attendee> attendees;

    // 참석자 저장, 수정
    private List<String> attendeeIds;
}
