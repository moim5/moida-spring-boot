# MOIDA - 모임 플랫폼

[![Deploy](https://img.shields.io/badge/Demo-Live-green)](https://moida-spring-boot.onrender.com)

**MOIDA**는 누구나 손쉽게 관심사 기반의 모임을 만들고, 참가자와 소통할 수 있는 웹 플랫폼입니다.  
모임 생성, 참가, 리뷰, Q&A, 파일 업로드, 관리자 페이지까지 모두 구현되어 있습니다.

---

## 1. 프로젝트 개요

- **프로젝트명**: MOIDA
- **목표**: 관심사 기반 모임 생성 및 참여, 소통이 가능한 웹 플랫폼
- **배포 주소**: [https://moida-spring-boot.onrender.com](https://moida-spring-boot.onrender.com)

---

## 2. 기획 의도

현대 사회는 취향이 다양해지고, 오프라인과 온라인을 넘나드는 만남이 많아졌지만,  
내가 원하는 모임이 언제·어디서 열리는지, 혹은 내가 연 모임을 어떻게 알릴 수 있을지 쉽게 알 수 없습니다.  
또한 모임을 열어도 예상 참가 인원을 파악하기 어려워 주저하는 경우가 많습니다.

**MOIDA**는 이러한 문제를 해결하기 위해 탄생했습니다.

- 누구나 쉽게 관심사 기반 모임을 생성
- 참가자와 소통(리뷰, Q&A) 가능
- 모바일 친화적인 반응형 UI/UX 구현

---

## 3. 기술 스택

| 구분 | 기술 |
|------|------|
| **언어** | Java |
| **백엔드** | Spring Boot (MVC), MyBatis |
| **프론트엔드** | Thymeleaf |
| **보안** | Spring Security, BCrypt 암호화 |
| **파일 저장소** | AWS S3 |
| **데이터베이스** | Oracle 18c (AWS EC2 + Docker) |
| **배포** | Render.com (Docker 컨테이너) |
| **개발 환경** | IntelliJ, VSCode, Spring Tools Suite |
| **형상 관리** | Git, GitHub |
| **빌드 도구** | Gradle |

---

## 4. 주요 기능

1. **회원가입 / 로그인**
    - Spring Security 기반 인증/인가
    - BCrypt로 비밀번호 암호화
2. **모임 관리**
    - 모임 생성, 조회, 참가, 삭제
3. **리뷰 / Q&A**
    - 모임 참가자 간 리뷰 작성
    - 질문과 답변 기능
4. **파일 업로드**
    - AWS SDK를 이용한 이미지 업로드 및 조회
5. **관리자 페이지**
    - 공지 등록
    - 회원 관리
    - 모임 관리

---

## 5. 실행 방법

```bash
# 저장소 클론
git clone https://github.com/moim5/moida-spring-boot.git
cd moida-spring-boot

# Gradle 빌드 및 실행
./gradlew build
./gradlew bootRun