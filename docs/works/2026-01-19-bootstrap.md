# 2026-01-19 작업 기록

## 작업 내용
- Spring Boot 4.0.1 프로젝트 기본 뼈대 재구성
- 기본 REST 상태 API(`/api/status`) 및 Thymeleaf 홈 화면 구성
- 보안 기본 구성(관리자 계정 미설정 시 전체 허용) 추가
- H2 로컬 프로파일 및 기본 설정 파일 추가

## 다음 단계
- 도메인 모델 설계(캠페인/자원/대상/실행)
- DB 스키마 및 JPA 매핑 설계
- RBAC 정책 구체화

## 추가 작업 (Configuration REST API)
- Genesys Config Server 연동 설정(`app.genesys.*`) 추가
- 현대 프로젝트 참고 소스를 기반으로 CRUD형 REST API 구성
	- 상담사, 그룹, 콜링리스트, 캠페인, DN, 트랜잭션, Place
- 공통 응답/예외 처리(503 포함) 정리

## 추가 작업 (Outbound REST API)
- Outbound Server 연동 설정(`app.outbound.*`) 추가
- 캠페인 로드/언로드/강제언로드/상태 조회, 다이얼링 시작/정지 REST API 구성
