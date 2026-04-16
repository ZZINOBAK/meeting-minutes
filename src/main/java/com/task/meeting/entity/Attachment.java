package com.task.meeting.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Attachment {
    private Long attachmentId;   // 파일 ID (PK)
    private Long meetingId;      // 회의록 ID   (FK)
    private String originalName; // 사용자가 올린 원래 파일명 (ex: 회의록.pdf)
    private String storedName;   // 서버에 저장된 겹치지 않는 이름 (UUID 등)
    private String filePath;     // 저장된 전체 경로
    private Long fileSize;       // 파일 크기
    private LocalDateTime createdAt;


}
