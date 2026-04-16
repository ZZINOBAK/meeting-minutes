package com.task.meeting.controller.api;

import com.task.meeting.entity.Meeting;
import com.task.meeting.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingAipController {

    @Value("${api.secret-key}")
    private String validApiKey;

    private final MeetingService meetingService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<Meeting> detail(@PathVariable("id") Long id) {
        // 1. 데이터 조회
        Meeting meeting = meetingService.findById(id);

        // 2. 데이터가 없을 경우 처리
        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        // 3. 성공 시 JSON 데이터와 함께 200 OK 응답
        return ResponseEntity.ok(meeting);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestParam("meeting") String meetingJson, // 1. 여기서 String으로 받는 게 핵심!
            @RequestPart(value = "file", required = false) List<MultipartFile> files,
            @RequestParam(value = "attendeeIds", required = false) List<String> attendeeIds) {

        // API 키 검증 (가져오신 yaml 설정값과 비교하세요)
        if (apiKey == null || !validApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        // 1. 여기서 개수 검사 (브라우저에서 실수로 여러 개 보냈을 때도 여기서 걸러짐)
        if (files != null && files.size() > 1) {
            return ResponseEntity.badRequest().body("파일은 한 개만 업로드 가능합니다.");
        }

        // 2. 서비스에는 원래 받던 대로 '한 개'만 꺼내서 던져줌
        MultipartFile file = (files != null && !files.isEmpty()) ? files.get(0) : null;

        try {
            // 2. 수동으로 JSON 문자열을 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            Meeting meeting = objectMapper.readValue(meetingJson, Meeting.class);

            // 3. 기존 서비스 호출 (재사용)
            meetingService.registerMeetingWithAttendees(meeting, file, attendeeIds);

            return ResponseEntity.ok("등록 성공");
        } catch (RuntimeException e) {
            // 서비스에서 던진 "PDF 파일만..." 혹은 "파일은 한 개만..." 메시지를 그대로 전달
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 그 외 진짜 예상치 못한 서버 에러(DB 연결 등) 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류 발생");
        }
    }


}
