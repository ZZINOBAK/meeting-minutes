package com.task.meeting.service;

import com.task.meeting.entity.Attendee;
import com.task.meeting.entity.Employee;
import com.task.meeting.repository.AttendeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeMapper attendeeMapper;


    public void registerAttendees(Long meetingId, List<String> empIds) {
        for (String empId : empIds) {
            Attendee attendee = new Attendee();
            attendee.setMeetingId(meetingId);
            attendee.setEmpino(empId);
            attendeeMapper.insertAttendee(attendee);
        }
    }

}
