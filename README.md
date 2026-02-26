# Gen Outbound Gateway

Genesys Engage OCS와 외부 시스템을 연결하는 Outbound Gateway입니다. Config/Outbound/Stat/T-Server 연동과 CCC 암복호화 파이프라인을 제공합니다.

## 구성
- Spring Boot 4.0.1 / Java 17
- Thymeleaf UI + REST API
- MS SQL Server (기본) / H2 (local 프로파일)
- OpenAPI(Swagger) 문서
- JWT 인증 및 요청/응답 암호화(AES256)

## 빠른 시작
1. Genesys SDK를 로컬 Maven 저장소에 설치합니다.
2. 필요한 환경 변수를 설정합니다.
3. `SPRING_PROFILES_ACTIVE=local` 로 실행하면 H2 메모리 DB를 사용합니다.

### 실행 스크립트
- PowerShell: `scripts/run-app.ps1`
- CMD: `scripts/run-app.cmd`

### JAR 실행 스크립트 (운영용)
- PowerShell: `scripts/run-jar.ps1`
- CMD: `scripts/run-jar.cmd`
- Linux: `scripts/run-jar.sh`

JAR 이름은 `outbound.jar`이며, 실행 스크립트와 같은 폴더에 두는 것을 기본으로 합니다.
`config` 폴더도 스크립트와 같은 폴더에 두면 자동으로 읽습니다.
필요 시 `JAR_PATH` 환경변수로 경로를 지정할 수 있습니다.

두 스크립트 모두 개발용 샘플 환경변수를 내장하고 있습니다. 운영 환경에서는 반드시 외부에서 값을 주입해 사용하세요.
한글 출력이 깨지면 스크립트를 UTF-8 BOM으로 저장하고 콘솔 코드페이지를 65001로 맞춰주세요.

### 운영 보안 분리 안내
- 운영 환경용 설정은 `config/application-prod.yml` 또는 `config/.env.prod`에 분리해 관리하세요.
- 예시는 `config/application-prod.yml.example`, `config/.env.prod.example`에 있습니다.
- `config/application-prod.yml`, `config/.env.prod`는 `.gitignore`에 포함되어 커밋되지 않습니다.

### Genesys SDK 로컬/사설 저장소 설치
빌드 전에 `lib/`의 Genesys SDK를 로컬 Maven 저장소에 설치해야 합니다. 설치 후에는 빌드 결과 JAR에 SDK가 포함되므로 런타임에 `lib/`를 별도로 배포하지 않아도 됩니다.

- Windows: `scripts/install-genesys-sdk-local.ps1`
- Linux: `scripts/install-genesys-sdk-local.sh`

사설 저장소를 쓰는 경우, `settings.xml`에 서버/미러 설정을 추가한 뒤 `deploy` 방식으로 업로드하세요. (인증 정보는 리포지토리에 커밋하지 마세요.)

### 주요 환경 변수
#### 프로파일/기능 토글
- `SPRING_PROFILES_ACTIVE`: 실행 프로파일 (기본 prod, local은 H2 사용)
- `APP_GENESYS_ENABLED`: Genesys Config 연동 활성화 여부
- `APP_OUTBOUND_ENABLED`: Outbound 연동 활성화 여부

#### 공통/로그
- `SERVER_PORT`: 서버 포트 (기본 8080)
- `LOGBACK_CONFIG_PATH`: Logback 설정 파일 경로 (기본 `./scripts/config/logback-spring.xml`)
- `LOG_DIR`: 로그 디렉터리 (기본 `./logs`)

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

#### Genesys Stat Server
- `GENESYS_STAT_ENABLED`: Stat Server 연동 활성화 여부
- `GENESYS_STAT_ENDPOINT_P`: 주 Stat Server endpoint
- `GENESYS_STAT_IP_P`: 주 Stat Server IP
- `GENESYS_STAT_PORT_P`: 주 Stat Server port
- `GENESYS_STAT_ENDPOINT_B`: 보조 Stat Server endpoint
- `GENESYS_STAT_IP_B`: 보조 Stat Server IP
- `GENESYS_STAT_PORT_B`: 보조 Stat Server port
- `GENESYS_STAT_CLIENT_NAME`: Stat Client 이름
- `GENESYS_STAT_TENANT_NAME`: Stat Tenant 이름
- `GENESYS_STAT_DEFAULT_STATISTIC`: 기본 Statistic
- `GENESYS_STAT_TIMEOUT_MS`: 요청 타임아웃(ms)
- `GENESYS_STAT_DELAY_MS`: 재시도 지연(ms)
- `GENESYS_STAT_ADDP_ENABLED`: ADP 사용 여부
- `GENESYS_STAT_ADDP_CLIENT_TIMEOUT`: ADP client timeout (seconds)
- `GENESYS_STAT_ADDP_SERVER_TIMEOUT`: ADP server timeout (seconds)

#### Genesys T-Server
- `GENESYS_TSERVER_ENABLED`: T-Server 연동 활성화 여부
- `GENESYS_TSERVER_ENDPOINT`: T-Server endpoint
- `GENESYS_TSERVER_IP`: T-Server IP
- `GENESYS_TSERVER_PORT`: T-Server port
- `GENESYS_TSERVER_ENDPOINT_B`: T-Server backup endpoint
- `GENESYS_TSERVER_IP_B`: T-Server backup IP
- `GENESYS_TSERVER_PORT_B`: T-Server backup port
- `GENESYS_TSERVER_CLIENT_NAME`: T-Server client 이름
- `GENESYS_TSERVER_CHARSET`: T-Server charset
- `GENESYS_TSERVER_ADDP_ENABLED`: ADP 사용 여부
- `GENESYS_TSERVER_ADDP_CLIENT_TIMEOUT`: ADP client timeout (seconds)
- `GENESYS_TSERVER_ADDP_SERVER_TIMEOUT`: ADP server timeout (seconds)

> Primary 접속 실패 시 backup 설정이 있으면 자동으로 backup으로 재시도합니다.

#### Outbound
- `OUTBOUND_URI`: Outbound 서버 URI
- `OUTBOUND_URI_B`: Outbound backup 서버 URI
- `OUTBOUND_CLIENT_NAME`: Outbound client 이름
- `OUTBOUND_CLIENT_PASSWORD`: Outbound client 비밀번호
- `OUTBOUND_APP_NAME`: Outbound app 이름
- `OUTBOUND_APP_PASSWORD`: Outbound app 비밀번호

> Primary 접속 실패 시 `OUTBOUND_URI_B`로 자동 재시도합니다.

#### Outbound Desktop (application 설정)
- `app.outbound.desktop.enabled`: Outbound Desktop 활성화
- `app.outbound.desktop.endpoint`: T-Server endpoint
- `app.outbound.desktop.ip`: T-Server IP
- `app.outbound.desktop.port`: T-Server port
- `app.outbound.desktop.client-name`: Client 이름
- `app.outbound.desktop.addp-enabled`: ADP 사용 여부
- `app.outbound.desktop.addp-client-timeout`: ADP client timeout (seconds)
- `app.outbound.desktop.addp-server-timeout`: ADP server timeout (seconds)
- `app.outbound.desktop.charset`: Charset
- `app.outbound.desktop.user-event-id`: User Event ID
- `app.outbound.desktop.listener-enabled`: 이벤트 수신 활성화
- `app.outbound.desktop.event-buffer-size`: 이벤트 버퍼 크기

## 엔드포인트
- `/` : 기본 UI
- `/api/status` : 상태 API
- `/swagger-ui` : Swagger UI
- `/v3/api-docs` : OpenAPI JSON
- `/actuator/health` : 헬스 체크
- `/auth/login` : JWT 토큰 발급
- `/auth/refresh` : JWT 토큰 갱신

### Configuration REST API (Genesys Config)
- `/api/v1/configuration/persons`, `/api/v1/configuration/agent-logins`
- `/api/v1/configuration/agent-groups`
- `/api/v1/configuration/calling-lists`, `/api/v1/configuration/filters`, `/api/v1/configuration/formats`
- `/api/v1/configuration/table-access`, `/api/v1/configuration/treatment`
- `/api/v1/configuration/campaigns`, `/api/v1/configuration/campaign-groups`
- `/api/v1/configuration/batch-create`
- `/api/v1/configuration/dns`, `/api/v1/configuration/dn-groups`
- `/api/v1/configuration/transactions`
- `/api/v1/configuration/places`, `/api/v1/configuration/place-groups`

> `app.genesys.enabled=true`와 Config Server 설정이 필요합니다.

### Outbound REST API
- `/api/v1/outbound/campaigns/load`
- `/api/v1/outbound/campaigns/unload`
- `/api/v1/outbound/campaigns/force-unload`
- `/api/v1/outbound/campaigns/status`
- `/api/v1/outbound/dial/start`
- `/api/v1/outbound/dial/stop`
- `/api/v1/outbound/health`

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

### CCC/Stat/T-Server 연동 API
- `/api/v1/stat/getSkillGrpStat` : 스킬그룹 상담사 상태 조회
- `/api/v1/voice/logout` : 상담사 로그아웃
- `/api/v1/voice/ready` : 상담사 대기
- `/api/v1/voice/notReady` : 상담사 이석
- `/api/v1/voice/checkStatus` : 상담사 상태 확인

> CCC 연동은 요청/응답 암호화(`CCC_SERVICE_ENC_*`) 설정을 따릅니다. 샘플 JSON은 `docs/works/2026-02-04-ccc-samples.md`를 참고하세요.

### 암복호화 테스트 API
- `/api/v1/crypto/encrypt` : AES256 암호화 테스트
- `/api/v1/crypto/decrypt` : AES256 복호화 테스트
- `/api/v1/crypto/secure/echo` : CCC 암복호화 파이프라인 에코

## 메모
- Genesys SDK는 로컬/사설 Maven 저장소에 설치한 뒤 `genesys-sdk` 프로파일로 참조합니다.
- 보안/권한은 추후 RBAC 설계에 맞춰 확장하세요.

## DB 사용자 인증
- 테이블: `app_users`
- 컬럼: `username`, `password_hash`, `roles`, `enabled`
- `roles`는 콤마로 구분하며 `ADMIN,OPERATOR` 형식 (자동으로 `ROLE_` 접두어 부여)
- 비밀번호는 `PasswordEncoder`(기본 bcrypt)로 인코딩된 값을 저장해야 합니다.
