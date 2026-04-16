package com.task.meeting.controller;

import com.task.meeting.entity.Attachment;
import com.task.meeting.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
        // 1. DB에서 파일 정보 가져오기
        Attachment attachment = fileService.getFileInfo(id);

        // 2. 파일 객체 생성
        Path path = Paths.get(attachment.getFilePath() + "/" + attachment.getStoredName());
        Resource resource = new FileSystemResource(path);

        // 3. 한글 파일명 깨짐 방지 처리
        String encodedName = UriUtils.encode(attachment.getOriginalName(), StandardCharsets.UTF_8);

        // 4. Content-Disposition 헤더 설정 (브라우저 다운로드 지시)
        String contentDisposition = "attachment; filename=\"" + encodedName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
