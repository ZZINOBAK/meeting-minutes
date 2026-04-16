package com.task.meeting.repository;

import com.task.meeting.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentMapper {

    // 첨부파일 정보 저장
    void insertAttachment(Attachment attachment);

    Attachment findById(Long id);

    Attachment findByMeetingId(Long meetingId);

    void deleteByMeetingId(Long meetingId);
}
