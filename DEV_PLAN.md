# Gen Outbound Gateway 개발 계획 (최신)

최종 업데이트: 2026-04-06

## 1) 프로젝트 목표

- Genesys Engage OCS 연동 게이트웨이를 안정적으로 운영한다.
- Config/Outbound/Stat/T-Server/SCS 기능을 REST API로 제공한다.
- JWT 인증 + CCC 암복호화 정책을 운영 환경에서 안전하게 유지한다.
- 문서(`README.md`, `docs/works`, `DEV_PLAN.md`)를 코드 변경과 함께 최신 상태로 유지한다.

## 2) 현재 아키텍처 요약

| 영역 | 현재 상태 |
| --- | --- |
| 애플리케이션 | Spring Boot 4.0.1 + Java 17 단일 애플리케이션 |
| API/UI | REST API + Thymeleaf 관리자 UI 공존 |
| 보안 | Spring Security + JWT + 권한 코드 기반 접근 제어 |
| 암복호화 | `@ConfigurationApiController` / `@CccEncryptedController` 경로에 encData 파이프라인 적용 |
| Genesys Config | 싱글톤 연결 + 스케줄 기반 health check + failover |
| Outbound/Stat/T-Server | 요청 시 연결(per-request) + failover |
| 데이터 | 기본 MS SQL Server, local 프로파일에서 H2 |

## 3) 최근 완료된 작업

### 보안/실행 스크립트

- `run-app`, `debugging-app`, `run-jar`의 CMD/SH 스크립트 보안 기준 정리
  - 비밀번호 계열 기본값 제거/주입 강제
  - 필수 환경변수 누락 시 실행 차단
  - prod 프로파일에서 `JWT_SECRET` 검증 강화
- JAR 실행 스크립트의 환경파일 로딩/경로 처리 정리

### API/기능

- `POST /auth/logout`를 요청 바디 기반으로 정리
- Outbound Config의 Treatment CRUD(`create/update/delete`) 반영
- Swagger 응답 예시/요청 예시 다수 정리

### 운영/문서

- OpenAPI 노출 필터링 정책(`OpenApiConfig`) 반영
- 클래스 역할 문서화(`package-info.java`) 추가
- `README.md` 최신화(실행 방법, 환경변수, API 요약, 운영 주의사항)

## 4) 진행 중/후속 우선순위

### P1 (운영 안정성)

1. `run-app.ps1` / `run-jar.ps1` 보안 정책 정합성 개선
	- 현재 CMD/SH 대비 민감값 기본값 처리 기준이 다름
	- 동일한 필수 변수 검증 정책으로 통일 필요
2. 운영 환경 변수 템플릿 정리
	- `scripts/config/.env.prod.example`와 `src/main/resources/application*.yml` 매핑 표 보강

### P2 (품질/테스트)

1. 인증/권한 회귀 테스트 보강
	- `/auth/login`, `/auth/refresh`, `/auth/logout`
	- `/api/v1/stat/**`, `/api/v1/voice/**` 권한 케이스
2. Genesys 연결 장애 시나리오 테스트 정리
	- primary 실패 → backup failover 확인
	- Config health-check 복구 동작 확인

### P3 (문서/개발 경험)

1. API 그룹별 사용 예시(JSON) 문서 분리
2. Swagger에 노출되지 않는 경로와 정책 사유 문서화
3. 배포 시나리오(소스 실행 vs JAR 실행) 체크리스트화

## 5) 기술 부채 및 리스크

- 스크립트(OS별) 보안 정책 불일치 가능성
- 민감값이 잘못된 기본값으로 실행될 위험
- Swagger 노출 정책과 실제 운영 정책 간 괴리 가능성
- Genesys SDK/Java 버전 의존성 유지 부담

## 6) 완료 기준 (Definition of Done)

- 기능 변경 시 다음을 모두 만족해야 완료로 간주
  1. 코드 반영 + 문서 반영(`README.md`, 필요 시 `docs/works`)
  2. 최소 컴파일 검증 통과(`mvn compile`)
  3. 영향 테스트 통과(최소 단위/통합 스모크)
  4. 보안 변수/운영 변수 체크 누락 없음

## 7) 실행/검증 기본 명령

```powershell
mvn compile
mvn verify
```

필요 시 로컬 실행:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
./scripts/run-app.ps1
```
