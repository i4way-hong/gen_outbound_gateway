# Gen Outbound Gateway

Genesys Engage OCS와 외부 시스템 사이를 연결하는 Outbound Gateway입니다.  
Config/Outbound/Stat/T-Server/SCS 연동, JWT 인증, CCC 요청·응답 암복호화 파이프라인을 제공합니다.

## 기술 스택

- Spring Boot `4.0.1`
- Java `17`
- Spring Security + JWT
- Spring Data JPA
- Thymeleaf + REST API
- OpenAPI(Swagger)
- DB: MS SQL Server(기본), H2(`local` 프로파일)

## 프로젝트 구조 요약

- `src/main/java/com/genoutbound/gateway/config`: 보안/암호화/JWT/OpenAPI 설정
- `src/main/java/com/genoutbound/gateway/core`: 공통 응답(`ApiResponse`), 예외(`ApiException`, `GlobalExceptionHandler`)
- `src/main/java/com/genoutbound/gateway/genesys`: Genesys 연동( cfg / outbound / stat / tserver / scs )
- `src/main/java/com/genoutbound/gateway/security`: 인증, 토큰, 사용자/권한
- `src/main/java/com/genoutbound/gateway/web`: 인증/상태/API 및 관리자 UI
- `src/main/resources`: `application*.yml` 설정 파일
- `scripts`: 실행/배포/SDK 설치 스크립트

## 사전 준비

### 1) Genesys SDK 로컬 설치

Genesys SDK JAR(`lib/`)은 빌드 전에 로컬 Maven 저장소에 설치되어야 합니다.

- Windows: `scripts/install-genesys-sdk-local.ps1`
- Linux/macOS: `scripts/install-genesys-sdk-local.sh`

`pom.xml`의 `genesys-sdk` 프로파일(기본 활성화)이 해당 아티팩트를 참조합니다.

### 2) Java 버전

- Java 17 권장/필수

## 실행 방법

### 소스 실행

- PowerShell: `scripts/run-app.ps1`
- CMD: `scripts/run-app.cmd`
- Linux/macOS: `scripts/run-app.sh`

### 디버그 실행(JDWP 5005)

- CMD: `scripts/debugging-app.cmd`
- Linux/macOS: `scripts/debugging-app.sh`

### JAR 실행(운영)

- PowerShell: `scripts/run-jar.ps1`
- CMD: `scripts/run-jar.cmd`
- Linux/macOS: `scripts/run-jar.sh`

기본 JAR 파일명은 `gen-outbound-gateway-0.0.1-SNAPSHOT.jar`입니다.  
필요 시 `JAR_PATH`로 경로를 지정할 수 있습니다.

JAR 스크립트는 `scripts/config/.env.prod`(없으면 `scripts/config/.env`)를 자동 로딩할 수 있습니다.

### 빠른 실행 예시

#### Windows PowerShell (로컬 개발)

```powershell
Set-Location "D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway"
$env:SPRING_PROFILES_ACTIVE="local"
./scripts/install-genesys-sdk-local.ps1
mvn clean compile
./scripts/run-app.ps1
```

#### Windows PowerShell (디버그, JDWP 5005)

```powershell
Set-Location "D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway"
$env:SPRING_PROFILES_ACTIVE="local"
./scripts/debugging-app.cmd
```

#### Linux/macOS (소스 실행)

```bash
cd /path/to/Gen_Outbound_Gateway
export SPRING_PROFILES_ACTIVE=local
./scripts/install-genesys-sdk-local.sh
mvn clean compile
./scripts/run-app.sh
```

#### 운영 JAR 실행 예시 (PowerShell)

```powershell
Set-Location "D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway"
$env:SPRING_PROFILES_ACTIVE="prod"
$env:ENV_FILE="./scripts/config/.env.prod"
./scripts/run-jar.ps1
```

> 참고: 실행 스크립트는 필수 환경변수(DB/관리자/Genesys 비밀번호 계열)가 없으면 실행을 중단합니다.

## 설정 파일 위치

- 기본: `src/main/resources/application.yml`
- 로컬: `src/main/resources/application-local.yml`
- 운영: `src/main/resources/application-prod.yml`
- 샘플 env: `scripts/config/.env.prod.example`

## 주요 환경 변수

### 공통

- `SPRING_PROFILES_ACTIVE` (기본: `prod`)
- `SERVER_PORT` (기본: `8080`)
- `LOGBACK_CONFIG_PATH` (기본: `file:./scripts/config/logback-spring.xml`)
- `LOG_DIR` (기본: `./logs`)

### DB

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

### 보안/인증

- `ADMIN_USERNAME`
- `ADMIN_PASSWORD`
- `AUTH_ENABLED`
- `JWT_ENABLED`
- `ALLOW_INSECURE`
- `ALLOW_SWAGGER`
- `ALLOW_CRYPTO_TEST`
- `ALLOW_ADMIN_UI`
- `JWT_ISSUER`
- `JWT_SECRET`
- `JWT_ACCESS_TOKEN_MINUTES`
- `JWT_REFRESH_TOKEN_DAYS`

### CCC 암복호화

- `CCC_SERVICE_ENC_ENABLED`
- `CCC_SERVICE_ENC_KEY` (32 bytes)
- `CCC_SERVICE_ENC_IV` (16 bytes)

### Genesys Config

- `GENESYS_CFG_ENDPOINT_P`, `GENESYS_CFG_IP_P`, `GENESYS_CFG_PORT_P`
- `GENESYS_CFG_ENDPOINT_B`, `GENESYS_CFG_IP_B`, `GENESYS_CFG_PORT_B`
- `GENESYS_CFG_CLIENT_NAME_B`
- `GENESYS_CFG_USERNAME`, `GENESYS_CFG_PASSWORD`
- `GENESYS_TENANT_DBID`
- `GENESYS_SWITCH_DBID_PRIMARY`, `GENESYS_SWITCH_DBID_SECONDARY`
- `GENESYS_CHARSET`
- `GENESYS_CFG_ADDP_ENABLED`
- `GENESYS_CFG_ADDP_CLIENT_TIMEOUT`
- `GENESYS_CFG_ADDP_SERVER_TIMEOUT`
- `GENESYS_CFG_HEALTH_CHECK_INTERVAL_MS`

### Genesys Outbound

- `OUTBOUND_URI`, `OUTBOUND_URI_B`
- `OUTBOUND_CLIENT_NAME`, `OUTBOUND_CLIENT_PASSWORD`
- `OUTBOUND_APP_NAME`, `OUTBOUND_APP_PASSWORD`

### Genesys Stat

- `GENESYS_STAT_ENABLED`
- `GENESYS_STAT_ENDPOINT_P`, `GENESYS_STAT_IP_P`, `GENESYS_STAT_PORT_P`
- `GENESYS_STAT_ENDPOINT_B`, `GENESYS_STAT_IP_B`, `GENESYS_STAT_PORT_B`
- `GENESYS_STAT_CLIENT_NAME`, `GENESYS_STAT_CHARSET`
- `GENESYS_STAT_TENANT_NAME`
- `GENESYS_STAT_DEFAULT_STATISTIC`
- `GENESYS_STAT_TIMEOUT_MS`, `GENESYS_STAT_DELAY_MS`

### Genesys T-Server

- `GENESYS_TSERVER_ENABLED`
- `GENESYS_TSERVER_ENDPOINT`, `GENESYS_TSERVER_IP`, `GENESYS_TSERVER_PORT`
- `GENESYS_TSERVER_ENDPOINT_B`, `GENESYS_TSERVER_IP_B`, `GENESYS_TSERVER_PORT_B`
- `GENESYS_TSERVER_CLIENT_NAME`, `GENESYS_TSERVER_CHARSET`

### Genesys SCS

- `GENESYS_SCS_ENABLED`
- `GENESYS_SCS_ENDPOINT_P`, `GENESYS_SCS_IP_P`, `GENESYS_SCS_PORT_P`
- `GENESYS_SCS_ENDPOINT_B`, `GENESYS_SCS_IP_B`, `GENESYS_SCS_PORT_B`
- `GENESYS_SCS_CLIENT_NAME`, `GENESYS_SCS_CLIENT_ID`
- `GENESYS_SCS_USERNAME`, `GENESYS_SCS_CHARSET`

## API 요약

### 기본/인증

- `GET /`
- `GET /api/status`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout` (요청 바디 기반 토큰 폐기)

### Genesys Config API (`/api/v1/configuration`)

- Agent Group: `/agent-groups*`
- Agent Login: `/agent-logins*`
- Person: `/persons*`
- Routing: `/dns*`, `/dn-groups*`, `/transactions*`, `/places*`, `/place-groups*`
- Outbound Config: `/calling-lists*`, `/filters*`, `/formats*`, `/campaigns*`, `/campaign-groups*`, `/table-access*`, `/treatment*`, `/batch-create`

> `treatment`는 조회뿐 아니라 `create/update/delete`를 포함합니다.

### Genesys Outbound API (`/api/v1/outbound`)

- `/campaigns/load`
- `/campaigns/unload`
- `/campaigns/force-unload`
- `/campaigns/status`
- `/dial/start`
- `/dial/stop`
- `/health`

### Stat / Voice / SCS / Crypto

- `POST /api/v1/stat/getSkillGrpStat`
- `POST /api/v1/voice/logout`
- `POST /api/v1/voice/ready`
- `POST /api/v1/voice/notReady`
- `POST /api/v1/voice/checkStatus`
- `POST /api/v1/crypto/encrypt`
- `POST /api/v1/crypto/decrypt`

## OpenAPI/Swagger

- Swagger UI: `/swagger-ui`
- OpenAPI JSON: `/v3/api-docs`

보안 설정(`ALLOW_SWAGGER`) 및 `OpenApiConfig`의 필터 정책에 따라 일부 경로/태그는 UI에서 숨겨질 수 있습니다.

## 헬스 체크 동작

- Config Server 클라이언트는 싱글톤 연결 + 주기적 헬스체크(`GENESYS_CFG_HEALTH_CHECK_INTERVAL_MS`)를 사용합니다.
- Outbound/Stat/T-Server는 요청 시 연결(per-request) 패턴 기반으로 동작합니다.
- 통합 상태는 `GET /api/status`에서 확인할 수 있습니다.

## Timeout/Retry 프로파일 가이드 (P3)

애플리케이션은 기동 시 timeout/retry 설정값을 fail-fast로 검증합니다.

- `app.genesys.addp-client-timeout`, `app.genesys.addp-server-timeout`: `1..300`
- `app.genesys.health-check-interval-ms`: `1000..600000`
- `app.stat.timeout-ms`: `100..120000`
- `app.stat.delay-ms`: `0..10000`
- `app.stat.addp-client-timeout`, `app.stat.addp-server-timeout`: `1..300`
- `app.tserver.addp-client-timeout`, `app.tserver.addp-server-timeout`: `1..300`

권장값(초기 운영 기준):

| 항목 | local | stage | prod |
| --- | ---: | ---: | ---: |
| `GENESYS_CFG_ADDP_CLIENT_TIMEOUT` / `SERVER_TIMEOUT` | 5 | 5 | 5 |
| `GENESYS_CFG_HEALTH_CHECK_INTERVAL_MS` | 30000 | 60000 | 120000 |
| `GENESYS_STAT_TIMEOUT_MS` | 5000 | 5000 | 5000 |
| `GENESYS_STAT_DELAY_MS` | 200 | 200 | 200 |
| `GENESYS_TSERVER_ADDP_CLIENT_TIMEOUT` / `SERVER_TIMEOUT` | 10 | 10 | 10 |

## 빌드/검증

- `mvn compile`
- `mvn verify` (Checkstyle + SpotBugs 포함)

## 운영 보안 주의사항

- `JWT_SECRET`, `CCC_SERVICE_ENC_KEY`, `CCC_SERVICE_ENC_IV`, 각종 비밀번호는 저장소에 커밋하지 마세요.
- 암복호화가 활성화된 컨트롤러(`@ConfigurationApiController`, `@CccEncryptedController`)는 평문 JSON 대신 `encData` 포맷을 사용해야 합니다.
- 인증(`AUTH_ENABLED`/`JWT_ENABLED`)을 끌 경우 `ALLOW_INSECURE=true` 정책을 반드시 검토하세요.

## 참고 문서

- 운영/개발 변경 기록: `docs/works/`
- 아키텍처 결정 기록: `docs/adr/`
