# PerformanceTest (k6)

Gen Outbound Gateway 성능 테스트를 위한 k6 시나리오 모음입니다.

## 구성

- `lib/common.js`: 공통 함수(환경변수, 헤더, 공통 체크)
- `scenarios/01-health-load.js`: 헬스 엔드포인트 부하 테스트 (기본 Load)
- `scenarios/02-auth-login-stress.js`: 로그인 엔드포인트 스트레스 테스트
- `scenarios/03-outbound-status-spike.js`: Outbound 상태조회 스파이크 테스트
- `scenarios/04-scs-sse-soak.js`: SCS SSE 연결 유지(Soak) 테스트
- `run-all.ps1`: Windows PowerShell용 일괄 실행 스크립트

## 사전 준비

1. 서버 기동 (`SPRING_PROFILES_ACTIVE=local` 권장)
2. k6 설치 및 PATH 등록
3. 필요 시 테스트 계정/토큰/요청 payload 준비

## 기본 환경변수

- `BASE_URL` (기본: `http://localhost:8080`)
- `AUTH_BEARER_TOKEN` (Bearer 토큰이 필요한 API에 사용)
- `AUTH_LOGIN_USERNAME`, `AUTH_LOGIN_PASSWORD` (로그인 시나리오)
- `OUTBOUND_STATUS_PAYLOAD` (JSON 문자열)

예시(`PowerShell`):

```powershell
$env:BASE_URL = "http://localhost:8080"
$env:AUTH_LOGIN_USERNAME = "admin"
$env:AUTH_LOGIN_PASSWORD = "admin123"
$env:OUTBOUND_STATUS_PAYLOAD = '{"campaignDbid":101}'
```

## 실행 예시

개별 실행:

```powershell
k6 run .\PerformanceTest\scenarios\01-health-load.js
k6 run .\PerformanceTest\scenarios\02-auth-login-stress.js
k6 run .\PerformanceTest\scenarios\03-outbound-status-spike.js
k6 run .\PerformanceTest\scenarios\04-scs-sse-soak.js
```

일괄 실행:

```powershell
.\PerformanceTest\run-all.ps1
```

## 결과 해석 포인트

- `http_req_failed`: 에러율
- `http_req_duration`: 지연시간(p95/p99)
- `checks`: 기능 검증 성공률
- 시나리오별 `thresholds` PASS/FAIL

## 주의사항

- 외부 연동(Genesys)이 포함되면 네트워크/외부 시스템 상태가 결과에 크게 영향.
- 비교 테스트 시 동일한 데이터/동일한 부하 프로필로 반복 측정 권장.
- SSE Soak는 연결 유지 안정성(끊김/타임아웃) 확인 목적.
