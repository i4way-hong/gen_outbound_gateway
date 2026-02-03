# Gen Outbound Gateway

Genesys Engage OCS와 외부 시스템을 연결하는 Outbound Gateway 프로젝트의 기본 뼈대입니다.

## 구성
- Spring Boot 4.0.1 / Java 17
- Thymeleaf UI + REST API
- MS SQL Server (기본) / H2 (local 프로파일)
- OpenAPI(Swagger) 문서

## 빠른 시작
1. 필요한 환경 변수를 설정합니다.
2. `SPRING_PROFILES_ACTIVE=local` 로 실행하면 H2 메모리 DB를 사용합니다.

### 실행 스크립트
- PowerShell: `scripts/run-app.ps1`
- CMD: `scripts/run-app.cmd`

두 스크립트 모두 기본 환경변수를 내장하고 있어 별도 설정 없이 실행할 수 있습니다. 외부에서 값을 지정하면 해당 값을 우선합니다.
한글 출력이 깨지면 스크립트를 UTF-8 BOM으로 저장하고 콘솔 코드페이지를 65001로 맞춰주세요.

### 주요 환경 변수
#### DB
- `DB_URL`: SQL Server JDBC URL
- `DB_USERNAME`: DB 사용자명
- `DB_PASSWORD`: DB 비밀번호

#### 관리자 계정
- `ADMIN_USERNAME`: 관리자 계정 (설정 시 JWT 인증 활성화)
- `ADMIN_PASSWORD`: 관리자 비밀번호

#### 인증 활성화
- `AUTH_ENABLED`: 인증 활성화 여부 (기본 true, false면 전체 허용)
- `JWT_ENABLED`: JWT 인증 활성화 여부 (기본 true, false면 전체 허용)

#### JWT 인증
- `JWT_SECRET`: JWT 서명 키(최소 32바이트)
- `JWT_ISSUER`: 토큰 발급자 (기본값: gen-outbound-gateway)
- `JWT_ACCESS_TOKEN_MINUTES`: 액세스 토큰 만료(분, 기본 15)
- `JWT_REFRESH_TOKEN_DAYS`: 리프레시 토큰 만료(일, 기본 7)

#### 응답/요청 암호화
- `CCC_SERVICE_ENC_ENABLED`: 암호화 사용 여부 (`true/false`)
- `CCC_SERVICE_ENC_KEY`: AES 키 (32 bytes)
- `CCC_SERVICE_ENC_IV`: AES IV (16 bytes)

#### Genesys Config
- `GENESYS_CFG_ENDPOINT_P`: 주 Config Server endpoint
- `GENESYS_CFG_IP_P`: 주 Config Server IP
- `GENESYS_CFG_PORT_P`: 주 Config Server port
- `GENESYS_CFG_ENDPOINT_B`: 보조 Config Server endpoint
- `GENESYS_CFG_IP_B`: 보조 Config Server IP
- `GENESYS_CFG_PORT_B`: 보조 Config Server port
- `GENESYS_CFG_CLIENT_NAME_B`: Config Client 이름
- `GENESYS_CFG_USERNAME`: Config Server 사용자명
- `GENESYS_CFG_PASSWORD`: Config Server 비밀번호
- `GENESYS_TENANT_DBID`: Tenant DBID
- `GENESYS_SWITCH_DBID_PRIMARY`: Switch DBID (primary)
- `GENESYS_SWITCH_DBID_SECONDARY`: Switch DBID (secondary)
- `GENESYS_CHARSET`: Config Client charset
- `GENESYS_CFG_ADDP_ENABLED`: ADP 사용 여부
- `GENESYS_CFG_ADDP_CLIENT_TIMEOUT`: ADP client timeout (seconds)
- `GENESYS_CFG_ADDP_SERVER_TIMEOUT`: ADP server timeout (seconds)
- `GENESYS_CFG_HEALTH_CHECK_INTERVAL_MS`: Config Server 헬스체크 주기(ms)

#### Outbound
- `OUTBOUND_URI`: Outbound 서버 URI
- `OUTBOUND_CLIENT_NAME`: Outbound client 이름
- `OUTBOUND_CLIENT_PASSWORD`: Outbound client 비밀번호
- `OUTBOUND_APP_NAME`: Outbound app 이름
- `OUTBOUND_APP_PASSWORD`: Outbound app 비밀번호

## 엔드포인트
- `/` : 기본 UI
- `/api/status` : 상태 API
- `/swagger-ui` : Swagger UI
- `/actuator/health` : 헬스 체크
- `/auth/login` : JWT 토큰 발급
- `/auth/refresh` : JWT 토큰 갱신

### Configuration REST API (Genesys Config)
- `/api/v1/configuration/persons`
- `/api/v1/configuration/agent-groups`
- `/api/v1/configuration/calling-lists`
- `/api/v1/configuration/campaigns`
- `/api/v1/configuration/dns`
- `/api/v1/configuration/transactions`
- `/api/v1/configuration/places`

> `app.genesys.enabled=true`와 Config Server 설정이 필요합니다.

### Outbound REST API
- `/api/v1/outbound/campaigns/load`
- `/api/v1/outbound/campaigns/unload`
- `/api/v1/outbound/campaigns/force-unload`
- `/api/v1/outbound/campaigns/status`
- `/api/v1/outbound/dial/start`
- `/api/v1/outbound/dial/stop`

> `app.outbound.enabled=true`와 Outbound Server 설정이 필요합니다.

### Outbound Desktop POC API
- `/api/v1/outbound/desktop/add-record`
- `/api/v1/outbound/desktop/add-record/ack`
- `/api/v1/outbound/desktop/do-not-call`
- `/api/v1/outbound/desktop/do-not-call/ack`
- `/api/v1/outbound/desktop/add-record/send`
- `/api/v1/outbound/desktop/add-record/ack/send`
- `/api/v1/outbound/desktop/do-not-call/send`
- `/api/v1/outbound/desktop/do-not-call/ack/send`
- `/api/v1/outbound/desktop/events`
- `/api/v1/outbound/desktop/events/clear`

> 전송 엔드포인트는 `app.outbound.desktop.enabled=true`와 T-Server 설정(`app.outbound.desktop.*`)이 필요합니다.
> 수신 엔드포인트는 `app.outbound.desktop.enabled=true`, `app.outbound.desktop.listener-enabled=true`와 동일한 T-Server 설정이 필요합니다.
> `/events`는 `messageType`, `userEventId`, `limit` 파라미터로 필터링할 수 있습니다.

## 메모
- Genesys Platform SDK JAR은 `lib/` 경로에 있으며 `genesys-sdk` 프로파일로 연결됩니다.
- 보안/권한은 추후 RBAC 설계에 맞춰 확장하세요.

## DB 사용자 인증
- 테이블: `app_users`
- 컬럼: `username`, `password_hash`, `roles`, `enabled`
- `roles`는 콤마로 구분하며 `ADMIN,OPERATOR` 형식 (자동으로 `ROLE_` 접두어 부여)
- 비밀번호는 `PasswordEncoder`(기본 bcrypt)로 인코딩된 값을 저장해야 합니다.
