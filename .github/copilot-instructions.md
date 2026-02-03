# Copilot Instructions for Gen Outbound Gateway

## 프로젝트 컨텍스트 요약
- 목적: Genesys Engage용 Outbound Gateway.
- 주요 기능: Genesys OCS와 외부 시스템 간 I/F 제공(REST API), 캠페인 자원 관리, 캠페인 대상 관리, 캠페인 실행 및 모니터링.
- 주요 사용자:
  - 공통: 로그인, 대시보드, 알림.
  - 관리자: 관리자 기능(사용자 관리, 권한 관리), 캠페인 설정, 자원 관리, 모니터링.
  - 운영자: 캠페인 실행, 실시간 모니터링, 리포트 확인.
- 프론트: thymeleaf
- 백엔드: Java Spring Boot 최신버전(REST API 중심, 금일기준 4.0.1이  최신버전임)
- "C:\Users\jkhong\Downloads\en-PSDK-9.0.x-Developer-book (7).pdf" 참조해서 개발 진행.
- 이 프로젝트를 개발하기 위해 필요한 Platform SDK는 "C:\Program Files\GCTI\Platform SDK for Java 9.0" 에 위치함. 이를 빌드 경로에 추가해서 개발 진행.
- DB: MS SQL Server 172.168.30.61:1433 ID: RND_USER PW: RND_USER DB: RND_TEST.
- 대용량 데이터 처리 고려. (콜 대상 업로드/검증/중복제거 파이프라인, 배치 처리 등)

## 작업 원칙
- 작은 PR/커밋: 기능 단위로 쪼개고 테스트 포함.
- 보안 우선: 비밀(.yml 등) 커밋 금지, 입력 검증, 최소 권한(RBAC), 업로드 제한.
- 성능 고려: 효율적 알고리즘, DB 인덱스, 캐싱 전략.
- 로깅/모니터링: 주요 이벤트 로깅, 오류 추적, 성능 모니터링.

## 코딩 규칙
- 언어/버전: 프론트 thymeleaf, 백엔드 Java Spring Boot 최신버전.
- 스타일: 일관된 코드 스타일 유지, Prettier/Checkstyle 사용.
- 네이밍: 명확하고 일관된 네이밍 컨벤션 준수.
- 주석: 복잡한 로직에만 주석, Javadoc 활용.
- 폴더 구조: 기능별 모듈화, 관련 파일 그룹화.
- 에러 처리: 일관된 에러 처리 전략, 사용자 친화적 메시지지향.
- secure 코딩: OWASP Top 10 준수, 입력 검증, 출력 인코딩.
- REST API 설계: RESTful 원칙 준수, 명확한 엔드포인트, 적절한 HTTP 메서드 사용, 보안 고려, 데이터 노출 방지, 적절한 상태 코드 반환, 데이터 포맷은 JSON 권장(암호화 여부와 암호화 방법 설정할 수 있도록 설계), 데이터 유효성 검사 철저, 데이터 암호화 필요 시 HTTPS 사용.
- REST API 문서화: Swagger/OpenAPI 사용, 최신 상태 유지.

## 문서 & 체계
- 변경 시 README/DEV_PLAN/GUIDELINES를 필요에 따라 업데이트.
- 매 단계 작업에 대한 정리를 /docs/works 폴더에 md 파일로 기록 권장.
- 아키텍처 결정은 ADR(간단한 md)로 기록 권장.

## Copilot 응답 스타일
- 한국어 우선, 필요 시 기술용 영문 병기.
- 짧고 구체적으로: 무엇을 했는지/할 건지, 영향, 확인 방법.
- 파일/심볼은 백틱(`)으로 감싸 언급.
- 명령어가 필요할 때만 코드블록으로, PowerShell 기준.
- 가능하면 실행/빌드/테스트까지 확인 후 결과 공유.

## 안전장치/금지사항
- 비밀키/토큰/비밀번호 생성·노출 금지.
- 임의의 외부 네트워크 호출 지양(필요 시 명시).
- 사용하지 않는 의존성 추가 금지.
