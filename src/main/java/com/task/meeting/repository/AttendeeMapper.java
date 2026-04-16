package com.task.meeting.repository;

import com.task.meeting.entity.Attendee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendeeMapper {

    void insertAttendee(Attendee attendee);

    void deleteAttendeesByMeetingId(Long meetingId);
}
