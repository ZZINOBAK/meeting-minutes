package com.task.meeting.service;

import com.task.meeting.dto.Criteria;
import com.task.meeting.entity.Attendee;
import com.task.meeting.entity.Meeting;
import com.task.meeting.repository.AttendeeMapper;
import com.task.meeting.repository.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingMapper meetingMapper;
    private final AttendeeMapper attendeeMapper;
    private final FileService fileService;

    @Transactional
    public void registerMeeting(Meeting meeting, MultipartFile file) {
        // 1. 회의록 본문 저장
        meetingMapper.insertMeeting(meeting);

        // 2. 파일이 있으면 새로 만든 메서드 호출
        if (file != null && !file.isEmpty()) {
            fileService.saveNewFile(meeting.getMeetingId(), file);
        }
    }

    @Transactional
    public void registerMeetingWithAttendees(Meeting meeting, MultipartFile file, List<String> attendeeIds) {
        // 1. 회의록 본문 저장
        meetingMapper.insertMeeting(meeting);

        // 2. 파일이 있으면 새로 만든 메서드 호출
        if (file != null && !file.isEmpty()) {
            fileService.saveNewFile(meeting.getMeetingId(), file);
        }

        if (attendeeIds != null && !attendeeIds.isEmpty()) {
            for (String empId : attendeeIds) {
                Attendee attendee = new Attendee();
                attendee.setMeetingId(meeting.getMeetingId()); // 저장된 회의 ID 세팅
                attendee.setEmpino(empId);
                attendeeMapper.insertAttendee(attendee);
            }
        }
    }

    public List<Meeting> findAllMeetings(Criteria cri) {
        List<Meeting> meetings = meetingMapper.findAllMeetings(cri);

        // 만약 데이터가 없을 때 null 대신 빈 리스트를 반환하고 싶다면
        // 아래와 같이 처리하기도 하지만, 마이바티스는 결과가 없으면 알아서 빈 리스트를 줍니다.
        return meetings;
    }

    public int getTotalCount(Criteria cri) {
        return meetingMapper.getTotalCount(cri);
    }

    public Meeting findById(Long id) {
        return meetingMapper.findById(id);
    }

    @Transactional
    public void update(Meeting meeting, List<String> attendeeIds, String deleteFile, MultipartFile newFile) {
        // 게시글 본문 정보 수정
        meetingMapper.update(meeting);

        // 기존 참석자 무조건 삭제
        attendeeMapper.deleteAttendeesByMeetingId(meeting.getMeetingId());

        // 3. [핵심] 현재 화면에 남겨진 참석자들을 새로 삽입
        if (attendeeIds != null && !attendeeIds.isEmpty()) {
            // 반복문을 돌며 insert. (MyBatis라면 가급적 Bulk Insert 권장)
            for (String empId : attendeeIds) {
                Attendee attendee = new Attendee();
                attendee.setMeetingId(meeting.getMeetingId()); // 저장된 회의 ID 세팅
                attendee.setEmpino(empId);
                attendeeMapper.insertAttendee(attendee);
            }
        }

        // 파일 처리 판단
        boolean isDeleteRequested = "Y".equals(deleteFile);
        boolean isNewFilePresent = (newFile != null && !newFile.isEmpty());

        // 삭제 요청이 있거나 새 파일이 들어오면 일단 기존 파일을 지움
        if (isDeleteRequested || isNewFilePresent) {
            fileService.deleteFile(meeting.getMeetingId());
        }

        // 3. 새 파일이 있으면 저장
        if (isNewFilePresent) {
            fileService.saveNewFile(meeting.getMeetingId(), newFile);
        }
    }

    @Transactional
    public void delete(Long id) {
        meetingMapper.delete(id);
    }


}
