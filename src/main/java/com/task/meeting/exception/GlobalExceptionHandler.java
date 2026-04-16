package com.task.meeting.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 서비스에서 throw new RuntimeException 한 것들을 여기서 잡습니다.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        // e.getMessage() -> "PDF 파일만 업로드할 수 있습니다" 등의 문구
        // 이걸 400(Bad Request) 에러와 함께 바디에 실어 보냅니다.
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // (선택) 아까 말한 파일 개수 초과 같은 것도 여기서 한 번에 처리 가능합니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
