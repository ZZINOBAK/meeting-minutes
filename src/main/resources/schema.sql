
-- 2. 사원 테이블 (부서, 직급 컬럼 합침)
CREATE TABLE T_EMPLOYEES (
    empino VARCHAR(10) PRIMARY KEY COMMENT '사번',
    empinm VARCHAR(20) NOT NULL COMMENT '이름',
    dept   VARCHAR(50) COMMENT '소속(부서)',
    position VARCHAR(30) COMMENT '직급'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 회의록 테이블
CREATE TABLE T_MEETING (
    meeting_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회의록 ID',
    title        VARCHAR(200) NOT NULL COMMENT '회의 제목',
    meeting_date DATETIME NOT NULL COMMENT '회의 일시',
    location     VARCHAR(100) COMMENT '회의 장소',
    empino       VARCHAR(50) NOT NULL COMMENT '작성자 사번',
    empinm       VARCHAR(20) COMMENT '작성자 이름', -- TEXT에서 VARCHAR로 최적화
    content      TEXT COMMENT '회의 내용',
    delete_at    DATETIME COMMENT '삭제일시',
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at   DATETIME COMMENT '수정일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 첨부파일 테이블
CREATE TABLE T_ATTACHMENT (
    attachment_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '첨부파일 ID',
    meeting_id    BIGINT NOT NULL COMMENT '회의록 ID',
    original_name VARCHAR(255) NOT NULL COMMENT '원본 파일명',
    stored_name   VARCHAR(255) NOT NULL COMMENT '저장 파일명 (UUID 등)',
    file_path     VARCHAR(500) NOT NULL COMMENT '파일 저장 경로',
    file_size     BIGINT NOT NULL COMMENT '파일 크기 (bytes)',
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    CONSTRAINT FK_MEETING_ATTACHMENT FOREIGN KEY (meeting_id)
        REFERENCES T_MEETING (meeting_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 회의 참석자 테이블 (다대다 관계 해소용)
CREATE TABLE T_MEETING_ATTENDEES (
    meeting_id BIGINT NOT NULL COMMENT '회의록 ID',
    empino     VARCHAR(10) NOT NULL COMMENT '사번',
    PRIMARY KEY (meeting_id, empino),
    CONSTRAINT FK_MEETING_ATTENDEES_ID FOREIGN KEY (meeting_id)
        REFERENCES T_MEETING (meeting_id) ON DELETE CASCADE,
    CONSTRAINT FK_MEETING_ATTENDEES_EMP FOREIGN KEY (empino)
        REFERENCES T_EMPLOYEES (empino)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;