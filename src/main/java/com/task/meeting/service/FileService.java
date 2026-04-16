package com.task.meeting.service;

import com.task.meeting.entity.Attachment;
import com.task.meeting.repository.AttachmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final AttachmentMapper attachmentMapper; // 파일 전용 매퍼가 있다고 가정

    public Attachment getFileInfo(Long id) {
        return attachmentMapper.findById(id);
    }

    @Transactional
    public void saveNewFile(Long meetingId, MultipartFile file) {
        // 1. PDF 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/json") && !contentType.equals("application/pdf")) {
            throw new RuntimeException("PDF 파일만 업로드할 수 있습니다.");
        }

        // 2. 경로 설정 (상대 경로를 절대 경로로 안전하게 변환)
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String absolutePath = folder.getAbsolutePath(); // 실제 물리 주소 확보

        // 3. 파일명 생성 (공백 제거)
        String originalName = file.getOriginalFilename();
        String cleanName = (originalName != null) ? originalName.replaceAll("\\s", "_") : "unnamed";
        String storedName = System.currentTimeMillis() + "_" + cleanName;

        try {
            // 4. 파일 저장 (File.separator를 사용해 경로와 파일명을 안전하게 결합)
            File destination = new File(absolutePath + File.separator + storedName);
            file.transferTo(destination);

            // 5. DB 저장 정보 세팅
            Attachment attachment = new Attachment();
            attachment.setMeetingId(meetingId);
            attachment.setOriginalName(originalName);
            attachment.setStoredName(storedName);
            attachment.setFilePath(absolutePath); // 나중에 파일을 찾기 쉽게 절대 경로 저장
            attachment.setFileSize(file.getSize());

            attachmentMapper.insertAttachment(attachment);

        } catch (IOException e) {
            // 이 메시지도 GlobalExceptionHandler가 잡아줄 거예요!
            throw new RuntimeException("서버에 파일을 저장하는 중 문제가 발생했습니다.", e);
        }
    }

    @Transactional
    public void deleteFile(Long meetingId) {
        Attachment oldFile = attachmentMapper.findByMeetingId(meetingId);

        if (oldFile != null) {
            // 1. NIO Paths를 사용하여 안전하게 경로 생성
            Path path = Paths.get(oldFile.getFilePath(), oldFile.getStoredName());
            File physicalFile = path.toFile();

            // 2. 파일이 실제로 존재할 때만 삭제 시도
            if (physicalFile.exists()) {
                boolean deleted = physicalFile.delete();
                if (!deleted) {
                    // 파일 삭제 실패 시 로그를 남기거나 예외를 던짐 (정책에 따라 선택)
                    // throw new RuntimeException("서버 파일 삭제에 실패했습니다.");
                    System.out.println("경고: 파일 삭제 실패 - " + path.toString());
                }
            }

            // 3. DB 정보 삭제 (순서를 파일 삭제 뒤에 두는 것이 데이터 정합성에 유리)
            attachmentMapper.deleteByMeetingId(meetingId);
        }
    }
}
