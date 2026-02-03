# Gen Outbound Gateway 개발 계획

## 1. 프로젝트 개요
- **목적**: Genesys Engage OCS와 외부 시스템 간 Outbound Gateway를 구축해 캠페인 자원/대상 관리, 실행, 모니터링을 REST API와 Thymeleaf UI로 제공.
- **범위**: 공통(로그인, 대시보드, 알림), 관리자(사용자/권한, 캠페인/자원 설정, 모니터링), 운영자(캠페인 실행, 실시간 모니터링, 리포트).
- **레퍼런스**: "lib/doc/en-PSDK-9.0.x-Developer-book.pdf"를 기반으로 OCS 연동 규격 확인.
- **구현 방향**: 단일 Spring Boot 4.0.1 애플리케이션에서 REST API와 Thymeleaf UI를 함께 제공.

## 2. 목표 및 성공 기준
1. 최신 Spring Boot 4.0.1 기반의 안정적인 REST API 제공.
2. 캠페인 자원/대상/실행 흐름 전체를 UI & API로 관리 가능.
3. RBAC, 입력 검증, 로깅 등 보안·운영 요건 충족.
4. Swagger/OpenAPI 문서 및 /docs/works에 작업기록 기록 최신 유지.

## 3. 아키텍처 방향
| 계층 | 주요 내용 |
| --- | --- |
| 애플리케이션 | Spring Boot 4.0.1 (Java 17+) 단일 프로젝트: REST Controller + Thymeleaf MVC, Service, Repository 3계층. |
| 통합 | Genesys OCS API 클라이언트 모듈(Platform SDK for Java 9.0 @ "C:\\Program Files\\GCTI\\Platform SDK for Java 9.0"), 외부 REST 연동, 메시지 큐(필요 시). |
| 데이터 | RDB (MS SQL Server) - 캠페인, 자원, 대상, 실행 로그 테이블 및 인덱스 설계. |
| 운영 | Spring Actuator, centralized logging, Prometheus exporter, RBAC(Security Config). |

## 4. 환경 변수 관리
운영 환경에서는 민감 정보 하드코딩을 금지하고 환경 변수로 주입합니다.

### DB
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

### 관리자 계정
- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`

### 응답/요청 암호화
- `CCC_SERVICE_ENC_ENABLED`
- `CCC_SERVICE_ENC_KEY`
- `CCC_SERVICE_ENC_IV`

### Genesys Config
- `GENESYS_CFG_ENDPOINT_P`
- `GENESYS_CFG_IP_P`
- `GENESYS_CFG_PORT_P`
- `GENESYS_CFG_ENDPOINT_B`
- `GENESYS_CFG_IP_B`
- `GENESYS_CFG_PORT_B`
- `GENESYS_CFG_CLIENT_NAME_B`
- `GENESYS_CFG_USERNAME`
- `GENESYS_CFG_PASSWORD`
- `GENESYS_TENANT_DBID`
- `GENESYS_SWITCH_DBID_PRIMARY`
- `GENESYS_SWITCH_DBID_SECONDARY`
- `GENESYS_CHARSET`

### Outbound
- `OUTBOUND_URI`
- `OUTBOUND_CLIENT_NAME`
- `OUTBOUND_CLIENT_PASSWORD`
- `OUTBOUND_APP_NAME`
- `OUTBOUND_APP_PASSWORD`

## 5. 추후 계획 (JWT 확장)
JWT 전환 완료 이후 확장해야 할 보안 기능을 정리합니다.

### 5.1 Refresh 토큰 저장소 (Redis/DB)
- **목표**: 리프레시 토큰 회수/만료/세션 관리 가능하도록 저장소 도입.
- **선택지**
	- Redis: TTL 자동 만료, 성능 우수 (권장)
	- DB: 감사 이력/장기 보관에 유리, 만료 정리 배치 필요
- **핵심 구현 항목**
	- JWT에 `jti`(토큰 ID) 클레임 추가
	- 로그인 시 `jti`를 저장소에 저장
	- `/auth/refresh`에서 저장소 유효성 검사 + 토큰 회전
	- 로그아웃/강제 만료 시 저장소 삭제

### 5.2 Role 기반 API 접근 제어
- **목표**: API별 접근 권한을 역할로 구분 (ADMIN/OPERATOR 등)
- **접근 방식**
	- URL 매핑: `requestMatchers().hasRole/hasAnyRole` 적용
	- 메서드 단위: `@PreAuthorize("hasRole('ADMIN')")` 적용
- **핵심 구현 항목**
	- JWT `roles` 클레임 기반 권한 매핑
	- 컨트롤러별 권한 정책 정의 및 문서화
	- Swagger 보안 요구사항 표기 (권장)
