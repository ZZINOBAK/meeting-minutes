# 📝 회의록 관리 시스템 (Meeting Minutes Management)

**Spring Boot 기반 회의록 관리 웹 애플리케이션**입니다.  
Nginx, Spring Boot, MariaDB를 도커 컨테이너로 구성하여 어디서든 일관된 환경으로 실행할 수 있도록 설계했습니다.

---

## 🚀 빠른 시작 (Quick Start)

이 프로젝트는 **Docker**와 **Docker Compose**가 설치된 환경에서 가장 빠르게 실행해볼 수 있습니다.

### 1. 프로젝트 클론
```bash
git clone https://github.com/ZZINOBAK/meeting-minutes.git
cd meeting-minutes
```

### 2. 환경 변수 설정 (.env)
보안을 위해 민감한 정보는 소스 코드에서 분리되어 있습니다.  
제공된 예시 파일을 복사하여 환경 변수 파일을 생성하세요.

```bash
cp .env.example .env
```

생성된 `.env` 파일 내의 비밀번호(DB_PASSWORD 등)를 환경에 맞게 수정하세요.

### 3. 도커 컴포즈 실행
아래 명령어를 입력하면 DB 준비가 완료된 후 애플리케이션이 자동으로 실행됩니다.

```bash
docker-compose up -d meeting-db
docker-compose up -d
```

### 4. 접속하기
브라우저를 열고 다음 주소로 접속하세요.

- **URL**: http://localhost (Nginx Proxy를 통해 80포트로 접속됩니다.)

---

## 🛠 기술 스택 (Tech Stack)

| 구분 | 기술 |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.x, MyBatis |
| **Database** | MariaDB 11 |
| **Frontend** | Thymeleaf, Vanilla JS, CSS3 |
| **DevOps** | Docker, Docker Compose, Nginx |

---

## 🏗 시스템 아키텍처 (Architecture)

이 프로젝트는 실제 운영 환경을 고려하여 **Multi-Container 아키텍처**를 채택했습니다.

- **Nginx**: 정적 리소스 서빙 및 리버스 프록시 역할 수행
- **Spring Boot**: 비즈니스 로직 처리 및 REST API 제공
- **MariaDB**: 데이터 영속성 관리

---

## 💡 주요 특징 (Key Highlights)

### 1. 환경별 설정 분리 (Profile Management)
- `local`: 로컬 개발 및 IntelliJ 실행 최적화
- `prod`: 도커 컨테이너 배포 환경 최적화

### 2. 보안 강화 (Security)
- `.env` 파일을 활용하여 DB 비밀번호 및 API Key 등 민감 정보 노출 차단
- `.gitignore`를 통한 체계적인 소스 제어

### 3. 데이터베이스 자동 초기화
- `schema.sql`과 `data.sql`을 통해 앱 실행 시 초기 테이블 및 샘플 데이터 자동 생성

---

## 📂 주요 디렉토리 구조

```text
.
├── src/main/java         # 서버 비즈니스 로직
├── src/main/resources    # 설정 파일 및 SQL 스크립트
├── nginx.conf            # Nginx 리버스 프록시 설정
├── Dockerfile            # Spring Boot 빌드 설정
├── docker-compose.yaml   # 멀티 컨테이너 정의서
└── .env.example          # 환경 변수 가이드 파일
```
