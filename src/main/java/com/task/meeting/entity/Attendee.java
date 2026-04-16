package com.task.meeting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attendee {
    private Long meetingId;
    private String empino;

    // 추가 정보
    private String empinm;
    private String dept;
    private String position;
}

