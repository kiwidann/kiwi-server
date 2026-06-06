# 🥝 Kiwi - CBT 기반 감정 기록 및 자기 인식 개선 서비스

## 📌 프로젝트 소개

Kiwi는 사용자의 감정 기록과 인지행동치료(CBT, Cognitive Behavioral Therapy) 기법을 결합하여
자신의 생각 패턴을 분석하고 건강한 사고로 개선할 수 있도록 돕는 서비스입니다

단순한 일기 작성 기능을 넘어
감정 → 생각 → 행동의 흐름을 구조적으로 기록하고
사용자가 스스로 자신의 사고를 점검할 수 있도록 설계되었습니다

---

## 🎯 주요 기능

### 1. 감정 기록 (Mood Tracking)

* 사용자가 하루의 감정을 선택 및 기록
* 감정 변화 흐름 시각화

### 2. CBT 기반 질문

* 상황, 생각, 감정, 행동을 단계적으로 기록
* 자동 질문을 통해 왜곡된 사고 인식 유도

### 3. 일기 작성

* 자유 형식의 일기 작성
* 감정 기록과 연결

### 4. 리포트 (분석)

* 감정 변화 통계 제공
* 반복되는 사고 패턴 분석

---

## 🛠 기술 스택

### Backend

* Java 17
* Spring Boot
* Spring Data JPA

### Database

* PostgreSQL

### Infra / DevOps

* Docker
* GitHub

---

## 🏗 프로젝트 구조

```text
src
 ┣ domain
 ┃ ┣ diary
 ┃ ┣ mood
 ┃ ┣ keyword
 ┃ ┣ cbt
 ┃ ┗ report
 ┣ global
 ┃ ┣ exception
 ┃ ┣ config
 ┃ ┗ util
 ┣ controller
 ┣ service
 ┗ repository
```

---

## 🧠 설계 개념

## 🗂 데이터베이스 구조

* users
* diaries
* mood_records
* cbt_records
* keywords
* reports

※ 상세 ERD는 docs/erd.md 참고

---

## 🚀 실행 방법

```bash
git clone https://github.com/your-repo/kiwi.git
cd kiwi
```

### 1. DB 설정

* PostgreSQL 실행
* application.yml 설정

### 2. 서버 실행

```bash
./gradlew bootRun
```

---

## 📏 Git Convention

### Commit

- ✨ feat: 기능 추가
- 🐛 fix: 버그 수정
- ♻️ refactor: 리팩토링
- 🔧 chore: 설정/기타 작업
- 📝 docs: 문서 수정

### Branch

* feat/{기능명}
* fix/{이슈명}

---

## 👥 역할 분담

- Backend (본 레포지토리)
    - API 설계 및 구현
    - DB 설계 및 관리
    - 인증/인가 처리
    - 비즈니스 로직 개발

- Frontend (별도 레포지토리)
    - UI/UX 구현
    - API 연동

※ 본 레포지토리는 Backend 서버만 포함되어 있으며, Frontend는 별도 레포지토리에서 관리됩니다.

---

## 📌 향후 계획

### 1차 목표 (핵심 기능 완성)
- 감정 기록 기능 구현
- CBT 질문/응답 흐름 구현
- 일기 작성 및 조회 기능 구현
- 기본 통계(리포트) 기능 구현

### 2차 목표 (기능 개선)
- 감정 데이터 기반 간단한 분석 기능 추가
- 키워드 추출 정확도 개선
- 사용자 경험(UX) 개선
---


