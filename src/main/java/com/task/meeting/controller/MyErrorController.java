package com.task.meeting.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // 413 에러(용량 초과)가 발생하면 templates/error/413.html을 보여줌
            if (statusCode == HttpStatus.PAYLOAD_TOO_LARGE.value()) {
                return "error/413";
            }
        }
        // 그 외 에러는 기본 에러 페이지로
        return "error";
    }
}
