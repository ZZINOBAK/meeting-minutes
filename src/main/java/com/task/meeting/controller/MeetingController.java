package com.task.meeting.controller;

import com.task.meeting.dto.Criteria;
import com.task.meeting.dto.PageDTO;
import com.task.meeting.entity.Meeting;
import com.task.meeting.service.AttendeeService;
import com.task.meeting.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final AttendeeService attendeeService;

    @GetMapping("/")
    public String index(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "type", required = false) String type,      // 검색 타입 추가
            @RequestParam(value = "keyword", required = false) String keyword, // 검색어 추가
            Model model) {

        // 1. 기준 설정 (3개씩 보기)
        Criteria cri = new Criteria(page, 5);
        cri.setType(type);       // 전달받은 타입 세팅
        cri.setKeyword(keyword); // 전달받은 키워드 세팅

        // 2. 서비스에서 데이터 가져오기 (이제 cri 안에 검색어가 들어있어서 SQL이 작동함!)
        List<Meeting> meetingList = meetingService.findAllMeetings(cri);
        int total = meetingService.getTotalCount(cri); // totalCount에도 cri를 넘겨야 검색된 개수가 나옵니다!

        // 3. 모델에 담기
        model.addAttribute("meetings", meetingList);
        model.addAttribute("pageMaker", new PageDTO(cri, total));

        return "index";
    }

    // 회의록 등록 페이지 이동
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("meeting", new Meeting());
        return "meeting/register";
    }

    // 2. 회의록 등록 처리
    @PostMapping("/register")
    public String register(@Valid Meeting meeting, BindingResult bindingResult,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam(value = "attendeeIds", required = false) List<String> attendeeIds,
                           Model model) {

        if (bindingResult.hasErrors()) {
            // 1. 에러들 중 첫 번째 에러의 메시지만 가져옴 (예: "사번은 숫자 10자리여야 합니다.")
            String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();

            // 2. 알림창에 띄울 메시지를 모델에 담음
            model.addAttribute("errorMessage", firstErrorMessage);

            // 3. 다시 입력 폼으로 이동 (데이터는 @ModelAttribute 덕분에 유지됨)
            return "meeting/register";
        }
//        meetingService.registerMeeting(meeting, file);
        meetingService.registerMeetingWithAttendees(meeting, file, attendeeIds);

        return "redirect:/meeting/";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        // id를 사용해 서비스에서 미팅 정보를 가져옵니다.
        Meeting meeting = meetingService.findById(id);

        // 가져온 데이터를 모델에 담아 상세 페이지(detail.html)로 보냅니다.
        model.addAttribute("meeting", meeting);

        return "meeting/detail"; // templates/detail.html 파일을 찾게 됩니다.
    }

    @GetMapping("/detail-json/{id}")
    public String detailJsonPage(@PathVariable("id") Long id, Model model) {
        // 1. 데이터를 조회하지 않습니다! (데이터는 나중에 JS가 가져올 거니까요)
        // 2. 오직 "어떤 ID를 조회할지" 정보만 화면에 넘겨줍니다.
        model.addAttribute("meetingId", id);

        // 3. 우리가 만든 detail-json.html 파일을 엽니다.
        return "meeting/detail-json";
    }

    @PostMapping("/update")
    public String update(Meeting meeting,
                         @RequestParam(value="attendeeIds", required=false) List<String> attendeeIds,
                         @RequestParam(value="deleteFile", required=false) String deleteFile,
                         @RequestParam(value="newFile", required=false) MultipartFile newFile,
                         RedirectAttributes redirectAttributes) {
        try {
            meetingService.update(meeting, attendeeIds, deleteFile, newFile);
//            return "redirect:/meeting/detail/" + meeting.getMeetingId();
            return "redirect:/meeting/detail-json/" + meeting.getMeetingId();
        } catch (RuntimeException e) {
            // 에러페이지로 이동
            return "error/";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        meetingService.delete(id);
        return "redirect:/meeting/"; // 삭제 후 목록으로 이동
    }
}
