package com.task.meeting.repository;

import com.task.meeting.dto.Criteria;
import com.task.meeting.entity.Attachment;
import com.task.meeting.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetingMapper {
    // 회의록 저장
    void insertMeeting(Meeting meeting);

    // 회의록 목록 조회 (Soft Delete 제외)
    List<Meeting> findAllMeetings(Criteria cri);

    int getTotalCount(Criteria cri);

    Meeting findById(Long id);

    void update(Meeting meeting);

    void delete(Long id);
}
