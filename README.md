# 🥝 Kiwi - 감정 기록 및 생각 정리 기반 자기 인식 서비스

## 📌 프로젝트 소개

Kiwi는 사용자가 자신의 감정과 생각을 기록하고, 이를 구조화된 질문을 통해 돌아볼 수 있도록 돕는 자기 인식 개선 서비스입니다

단순히 하루의 감정을 기록하는 것에서 그치지 않고, 감정이 발생한 상황과 그때 떠오른 생각을 함께 정리할 수 있도록 설계되었습니다

사용자는 감정 기록, 일기 작성, 생각정리도구, 리포트 기능을 통해 자신의 감정 흐름과 반복되는 생각 패턴을 확인할 수 있습니다

Kiwi는 전문적인 진단이나 치료를 제공하는 서비스가 아니라, 사용자가 스스로 자신의 생각과 감정을 점검하고 정리할 수 있도록 돕는 보조 도구를 목표로 합니다

---

## 🌐 배포 주소

- API Server: https://api.kiwi-server.uk
- Swagger UI: https://api.kiwi-server.uk/swagger-ui/index.html

> Swagger UI는 포트폴리오 확인 및 API 테스트를 위해 임시 공개 중입니다

---

## 🧩 핵심 용어

Kiwi에서는 감정 기록을 더 명확하게 분류하기 위해 감정 키워드와 태그를 구분합니다

| 용어 | 의미 | 예시 |
| --- | --- | --- |
| 감정 키워드 | 사용자가 느낀 감정이나 상태를 나타내는 단어 | 우울함, 기쁨, 불안, 답답함 |
| 태그 | 감정이 발생한 맥락이나 주제를 나타내는 분류 | 인간관계, 학업, 진로, 가족 |

---

## 🎯 주요 기능

### 1. 감정 기록

- 사용자가 하루 동안 느낀 감정을 기록
- 감정 키워드를 통해 감정 상태를 분류
- 날짜별 감정 기록 조회
- 감정 기록을 기반으로 자신의 감정 흐름을 확인

### 2. 생각정리도구

사용자는 감정이나 고민 상황에 따라 총 6개의 생각정리도구를 사용할 수 있습니다

생각정리도구는 정답을 제시하는 기능이 아니라, 사용자가 자신의 생각을 다른 관점에서 바라보고 정리할 수 있도록 돕는 질문 기반 도구입니다

#### 제공 도구

| 번호 | 도구명 | 목적 |
| --- | --- | --- |
| 1 | 생각 다시 보기 | 떠오른 생각과 그 근거를 점검하고, 반대되는 근거를 함께 살펴봅니다 |
| 2 | 다른 시선에서 바라보기 | 같은 상황을 친구나 제3자의 관점에서 다시 해석해봅니다 |
| 3 | 느낌과 사실 나누기 | 감정과 실제 확인 가능한 사실을 구분합니다 |
| 4 | 최악의 생각 살펴보기 | 걱정하는 최악의 결과와 실제 가능성, 대처 방법을 정리합니다 |
| 5 | 마음 쉬어가기 | 마음을 안정시키는 기억, 작은 목표, 자기 위로 문장을 정리합니다 |
| 6 | 호흡하고 안정 찾기 | 호흡과 주변 감각에 집중하며 현재 상태를 점검합니다 |

### 3. 일기 작성

- 자유 형식의 일기 작성
- 감정 기록과 연결
- 태그를 통해 일기 내용과 관련된 맥락 분류
- 날짜별 일기 조회 및 관리

### 4. 리포트

- 감정 기록 기반 통계 제공
- 반복적으로 등장하는 감정 키워드 분석
- 생각정리도구 사용 기록 기반 자기 인식 리포트 제공

### 5. 알림

- 리포트 관련 알림 기능 제공
- 사용자별 알림 조회 및 관리

### 6. 인증/인가

- 이메일 기반 회원가입 및 로그인
- JWT 기반 인증 처리
- Refresh Token 기반 토큰 재발급
- Redis를 활용한 Refresh Token 저장

---

## 🛠 기술 스택

### Backend

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Spring Mail
- Springdoc OpenAPI

### Database

- PostgreSQL
- Redis

### Infra / DevOps

- AWS EC2
- Docker
- Docker Compose
- DockerHub
- GitHub Actions
- Nginx
- Let's Encrypt
- Cloudflare DNS
- Elastic IP

---

## ☁️ 배포 구조

Kiwi 백엔드 서버는 AWS EC2 환경에 Docker Compose 기반으로 배포되어 있습니다

```text
Client
  ↓
Cloudflare DNS
  ↓
HTTPS
  ↓
Nginx Reverse Proxy
  ↓
Spring Boot Application Container
  ├─ PostgreSQL Container
  └─ Redis Container
```

### 배포 구성

* AWS EC2 인스턴스에서 백엔드 서버 실행
* Docker Compose를 사용하여 Spring Boot, PostgreSQL, Redis 컨테이너 구성
* DockerHub에 저장된 백엔드 이미지를 EC2에서 pull하여 실행
* Nginx를 리버스 프록시로 사용하여 HTTPS 요청을 Spring Boot 서버로 전달
* Let's Encrypt 인증서를 사용하여 HTTPS 적용
* Elastic IP를 연결하여 서버 IP 고정
* Cloudflare DNS를 통해 `api.kiwi-server.uk` 도메인 연결
* Spring Boot 8080 포트는 외부에 직접 노출하지 않고 EC2 내부에서만 접근하도록 제한

---

## 🔄 CI/CD

GitHub Actions를 사용하여 `main` 브랜치 기준 자동 배포 파이프라인을 구성했습니다

```text
main 브랜치 push 또는 PR merge
  ↓
GitHub Actions 실행
  ↓
Spring Boot 프로젝트 빌드
  ↓
Docker 이미지 생성
  ↓
DockerHub에 이미지 push
  ↓
EC2 서버에 SSH 접속
  ↓
docker compose pull
  ↓
docker compose up -d
  ↓
배포 완료
```

### CI/CD 구성

* GitHub Actions 기반 자동 배포
* DockerHub 이미지 저장소 사용
* EC2 서버에서 Docker Compose로 최신 이미지 재배포
* GitHub Actions 배포에 필요한 접속 정보는 GitHub Secrets로 관리
* 애플리케이션 실행에 필요한 민감 정보는 서버 환경변수로 관리

### GitHub Secrets

```text
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN
EC2_HOST
EC2_USERNAME
EC2_SSH_KEY
```

---

## 🏗 프로젝트 구조

```text
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.kiwi.kiwiserver
 ┃ ┃   ┣ domain
 ┃ ┃   ┃ ┣ dailyrecord
 ┃ ┃   ┃ ┃ ┣ diary
 ┃ ┃   ┃ ┃ ┣ keyword
 ┃ ┃   ┃ ┃ ┣ record
 ┃ ┃   ┃ ┃ ┗ thinkingtool
 ┃ ┃   ┃ ┣ identity
 ┃ ┃   ┃ ┃ ┣ account
 ┃ ┃   ┃ ┃ ┣ common
 ┃ ┃   ┃ ┃ ┗ user
 ┃ ┃   ┃ ┣ item
 ┃ ┃   ┃ ┣ kiwitransaction
 ┃ ┃   ┃ ┗ report
 ┃ ┃   ┃   ┣ alert
 ┃ ┃   ┃   ┗ report
 ┃ ┃   ┣ global
 ┃ ┃   ┃ ┣ config
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┣ exception
 ┃ ┃   ┃ ┣ mail
 ┃ ┃   ┃ ┣ response
 ┃ ┃   ┃ ┗ security
 ┃ ┃   ┗ KiwiServerApplication
 ┃ ┗ resources
 ┃   ┣ db
 ┃   ┃ ┗ migration
 ┃   ┣ application.yml
 ┃   └ application-local.yml
 ┗ test
```

---

## 🗂 데이터베이스 구조

주요 테이블은 다음과 같습니다

```text
accounts
users
records
diaries
keywords
record_keywords
tags
cbt_sessions
cbt_questions
cbt_answers
report_alerts
items
item_categories
user_items
user_equipped_items
kiwi_transactions
```

생각정리도구 관련 데이터는 `cbt_sessions`, `cbt_questions`, `cbt_answers` 테이블에서 관리합니다

마이그레이션은 Flyway를 사용하여 관리합니다

```text
src/main/resources/db/migration
```

---

## 🔐 인증 구조

Kiwi는 JWT 기반 인증 방식을 사용합니다

```text
로그인 성공
  ↓
Access Token 발급
  ↓
Refresh Token 발급
  ↓
Refresh Token Redis 저장
  ↓
Access Token 만료 시 Refresh Token으로 재발급
```

### 인증 구성

* Access Token: API 인증에 사용
* Refresh Token: 토큰 재발급에 사용
* Redis: Refresh Token 저장소로 사용
* Spring Security: 인증 필터 및 접근 제어 처리

---

## 🚀 로컬 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/kiwidann/kiwi-server.git
cd kiwi-server
```

### 2. PostgreSQL 및 Redis 준비

로컬 환경에서 PostgreSQL과 Redis가 실행되어 있어야 합니다

Docker로 실행하는 경우 예시:

```bash
docker run -d --name kiwi-postgres -p 5433:5432 \
  -e POSTGRES_DB=kiwi \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=your_password \
  postgres:16
```

```bash
docker run -d --name kiwi-redis -p 6379:6379 redis:7
```

### 3. 환경변수 설정

로컬 실행 시 다음 환경변수가 필요합니다

```env
DB_URL=jdbc:postgresql://localhost:5433/kiwi
DB_USERNAME=postgres
DB_PASSWORD=your_password

REDIS_HOST=localhost
REDIS_PORT=6379

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

JWT_SECRET=your_jwt_secret
JWT_ACCESS_TOKEN_EXPIRATION_MS=3600000
JWT_REFRESH_TOKEN_EXPIRATION_MS=1209600000
```

### 4. 서버 실행

```bash
./gradlew bootRun
```

Windows PowerShell에서는 다음과 같이 실행할 수 있습니다

```powershell
.\gradlew bootRun
```

---

## 🐳 Docker 실행 방법

### 1. Docker 이미지 빌드

```bash
docker build -t kiwi-server .
```

### 2. Docker 컨테이너 실행

로컬 PC의 PostgreSQL과 Redis에 접근하기 위해 Docker 컨테이너에서는 `host.docker.internal`을 사용합니다

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e DB_URL="jdbc:postgresql://host.docker.internal:5433/kiwi" \
  -e DB_USERNAME="postgres" \
  -e DB_PASSWORD="your_password" \
  -e REDIS_HOST="host.docker.internal" \
  -e REDIS_PORT="6379" \
  -e MAIL_USERNAME="your_email@gmail.com" \
  -e MAIL_PASSWORD="your_app_password" \
  -e JWT_SECRET="your_jwt_secret" \
  kiwi-server
```

Windows PowerShell에서는 한 줄로 실행하는 것을 권장합니다

```powershell
docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local -e DB_URL="jdbc:postgresql://host.docker.internal:5433/kiwi" -e DB_USERNAME="postgres" -e DB_PASSWORD="your_password" -e REDIS_HOST="host.docker.internal" -e REDIS_PORT="6379" -e MAIL_USERNAME="your_email@gmail.com" -e MAIL_PASSWORD="your_app_password" -e JWT_SECRET="your_jwt_secret" kiwi-server
```

---

## 🧾 Docker Compose 배포 구성

EC2 서버에서는 Docker Compose를 사용하여 다음 컨테이너를 실행합니다

```text
kiwi-server
kiwi-postgres
kiwi-redis
```

예시 구성:

```yml
services:
  kiwi-server:
    image: dhgpfla02/kiwi-server:latest
    container_name: kiwi-server
    ports:
      - "127.0.0.1:8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      PORT: 8080

      DB_URL: jdbc:postgresql://kiwi-postgres:5432/kiwi
      DB_USERNAME: postgres
      DB_PASSWORD: your_password

      REDIS_HOST: kiwi-redis
      REDIS_PORT: 6379

      MAIL_USERNAME: your_email@gmail.com
      MAIL_PASSWORD: your_app_password

      JWT_SECRET: your_jwt_secret
      JWT_ACCESS_TOKEN_EXPIRATION_MS: 3600000
      JWT_REFRESH_TOKEN_EXPIRATION_MS: 1209600000
    depends_on:
      - kiwi-postgres
      - kiwi-redis
    restart: always

  kiwi-postgres:
    image: postgres:16
    container_name: kiwi-postgres
    environment:
      POSTGRES_DB: kiwi
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: your_password
    volumes:
      - kiwi-postgres-data:/var/lib/postgresql/data
    restart: always

  kiwi-redis:
    image: redis:7
    container_name: kiwi-redis
    restart: always

volumes:
  kiwi-postgres-data:
```

---

## 🔎 API 문서

Swagger UI를 통해 API 명세를 확인할 수 있습니다

```text
https://api.kiwi-server.uk/swagger-ui/index.html
```

Swagger에서는 각 API의 요청 URL, HTTP Method, Request Body, Response Body를 확인할 수 있으며 직접 API 테스트가 가능합니다

---

## 📏 Git Convention

### Commit

```text
✨ feat: 기능 추가
🐛 fix: 버그 수정
♻️ refactor: 리팩토링
🔧 chore: 설정 및 기타 작업
📝 docs: 문서 수정
🚀 deploy: 배포 관련 작업
💚 ci: CI/CD 관련 작업
```

### Branch

```text
feat/{기능명}
fix/{이슈명}
chore/{작업명}
docs/{문서명}
```

### Pull Request

PR 제목은 다음 형식을 사용합니다

```text
[Type] 작업 내용
```

예시:

```text
[CI/CD] EC2 자동 배포 워크플로우 추가
[Docs] README 배포 및 CI/CD 문서화
```

---

## 👥 역할 분담

### Backend

본 레포지토리는 Kiwi 서비스의 Backend 서버를 담당합니다

* API 설계 및 구현
* DB 설계 및 관리
* 인증/인가 처리
* JWT 및 Refresh Token 관리
* 생각정리도구 기록 및 감정 기록 비즈니스 로직 구현
* 리포트 및 알림 기능 구현
* 배포 및 CI/CD 구성

### Frontend

Frontend는 별도 레포지토리에서 관리됩니다

---

## 📌 향후 계획

### 1차 목표

* 감정 기록 기능 고도화
* 생각정리도구 질문/응답 흐름 개선
* 일기 작성 및 조회 기능 개선
* 리포트 통계 기능 개선

### 2차 목표

* 프론트엔드 연동을 위한 API 안정화
* 사용자 경험 개선
* Swagger 운영 환경 접근 제한
* SSH 접근 보안 개선

### 3차 목표

* PostgreSQL을 AWS RDS로 분리
* DB 백업 전략 구성
* 로그 및 모니터링 도입
* 운영 환경 안정화

---

## ⚠️ 운영 참고 사항

* 현재 PostgreSQL과 Redis는 EC2 내부 Docker 컨테이너로 실행됩니다
* PostgreSQL 데이터는 Docker volume에 저장됩니다
* 운영 안정성을 높이기 위해 추후 PostgreSQL을 AWS RDS로 분리할 예정입니다
* HTTPS는 Nginx와 Let's Encrypt를 통해 적용되어 있습니다
* Spring Boot 8080 포트는 외부에 직접 노출하지 않고 Nginx를 통해서만 접근하도록 구성했습니다
* 민감 정보는 코드에 포함하지 않고 환경변수 및 GitHub Secrets로 관리합니다
