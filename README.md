# 🕵️ 카카오프렌즈 미스터리: "황금 콘 도난 사건" (Backend)

LLM과 RAG 기술을 결합한 인터랙티브 추리 게임의 백엔드 서버입니다.

## 🛠 Tech Stack
- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security (카카오 OAuth2 연동 예정)

## ⚙️ 로컬 실행 방법 (How to Run)
1. **DB 설정**: 로컬 PostgreSQL에 `kakao_mystery` 데이터베이스를 생성하세요.
2. **환경 변수**: 실행 시 시스템 환경 변수에 아래 항목을 설정해야 합니다.
   - `DB_PASSWORD`: 본인의 PostgreSQL 비밀번호
3. **접속**: `http://localhost:8080` (현재 개발용으로 모든 API 권한이 열려 있습니다.)
-
