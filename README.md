# 🤖 AI Chatbot 시스템

> 인공지능을 기반으로 한 차세대 고객센터 시스템  
> ✅ 빠른 응답 속도 | ✅ 인건비 절감 | ✅ 실시간 피드백 시각화

---

## 🧩 프로젝트 소개

AI Chatbot 는 [Alibaba 통의천문 (qwen-plus)] API를 활용한 **지능형 응답 시스템**입니다.  
USER 의 질문을 이해하고, 검색 및 생성형 AI를 통해 빠르게 응답하며,  
운영자는 피드백 데이터를 통해 응답 품질을 분석할 수 있습니다.

> 💡 *Spring Boot + Vue.js 기반의 통합형 백오피스 지원*

---

## 🔧 주요 기능

### ✅ 후방 시스템 (Spring Boot 기반)
- [x] JWT 인증 및 사용자 관리
- [x] AI 응답 처리 (통의천문 API 연동)
- [x] MinIO 기반 파일 업로드/아바타 저장
- [x] Redis + RabbitMQ 통합
- [x] Swagger / Knife4j API 문서 자동 생성

### ✅ 프론트엔드 (Vue.js 기반)
- [x] 로그인 및 회원가입 화면
- [x] 실시간 AI 채팅 인터페이스
- [x] 사용자 정보 관리 및 설정

---

## 🛠 기술 스택

| 분류 | 사용 기술 |
|------|------------|
| Backend | `Java 17`, `Spring Boot`, `MyBatis Plus`, `Redis`, `RabbitMQ`, `MinIO`, `Spring Security`, `JWT` |
| AI | `阿里云 通义千问 API (qwen-plus)` |
| Frontend | `Vue.js`, `Axios`, `Vuex/Pinia`, `Element UI` |
| DevOps | `Docker`, `Jenkins`, `Nginx`, `MySQL 8.0` |
