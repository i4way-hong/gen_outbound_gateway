# API Method 통합(POST) 전환 계획

작성일: 2026-03-03

## 요청 요약
- 모든 API의 `GET`을 `POST`로 변경
- `POST` 방식 중 `@RequestParam` 파라미터를 사용하는 API는 **request body**로 변경
- 모든 `PUT`/`DELETE`를 `POST`로 변경

## 범위/대상 API(현황)
아래는 현재 코드베이스에서 HTTP Method 기반으로 정리한 영향 범위입니다.

### 공통/상태/보안
- `ApiStatusController`
  - `GET /api/status`
- `ScsSseController`
  - `GET /api/v1/scs/app-status/stream`
  - `GET /api/v1/scs/metrics`
  - `GET /api/v1/scs/app-status?dbid=...`
- `CryptoTestController`, `CryptoSecureEchoController`
  - `/api/v1/crypto/**` (GET/POST 혼재 가능)
- `AuthController`
  - `/auth/login`, `/auth/refresh`, `/auth/logout` (POST)

### Genesys Configuration API (`/api/v1/configuration`)
- `AgentGroupController`
  - `GET /agent-groups`, `GET /agent-groups/{groupDbid}`, `GET /agent-groups/by-name`
  - `PUT /agent-groups/{groupDbid}`
  - `DELETE /agent-groups/{groupDbid}`
- `AgentLoginController`
  - `GET /agent-logins`, `GET /agent-logins/{loginCode}`
  - `PUT /agent-logins/{loginCode}`
  - `DELETE /agent-logins/{loginCode}`
- `OutboundConfigController`
  - 다수 `GET /...` 조회 API
  - `PUT /filters/{filterDbid}`, `DELETE /filters/{filterDbid}`
  - `PUT /calling-lists/{callingListDbid}`, `DELETE /calling-lists/{callingListDbid}`
  - `PUT /campaign-groups/{groupDbid}`, `DELETE /campaign-groups/{groupDbid}`
  - `PUT /campaigns/{campaignDbid}`, `DELETE /campaigns/{campaignDbid}`
- `PersonController`
  - `GET /persons`, `GET /persons/{personDbid}`, `GET /persons/by-employee`
  - `PUT /persons/{personDbid}`
  - `DELETE /persons/{personDbid}`
  - `DELETE /persons/{personDbid}/skills`
- `RoutingConfigController`
  - 다수 `GET /dns`, `/dn-groups`, `/transactions`, `/places`, `/place-groups` 조회 API
  - `PUT`/`DELETE` 다수

### Genesys Outbound API (`/api/v1/outbound`)
- `OutboundController`
  - `GET /health`
- `OutboundDesktopController`
  - `GET /events` (POC, 현재 @RestController 주석 상태)

## 전환 정책 제안
### 1) Method 통합 정책
- 모든 API를 `POST`로 통일
- 기존 경로는 유지하되 **method만 변경**
- 필요한 경우 `POST` + `/query` 처럼 의미 있는 suffix 추가 검토
- **Body가 없는 조회 API는 GET 유지** (예: `/api/status`, `/api/v1/scs/metrics`)

### 2) 파라미터 전달 정책
- 기존 `@RequestParam`은 전부 **request body DTO로 이동**
- 규칙: `Query`/`Search`/`Filter` 접미 DTO를 생성
  - 예: `PersonSearchRequest`, `CallingListQueryRequest`

### 3) Idempotent 동작 유지
- 조회/검색은 여전히 **읽기 성격**을 유지하지만 method는 POST
- 응답 캐싱/로그/모니터링에서 GET 기반 가정을 제거

### 4) API 변경 영향
- Swagger(OpenAPI) 전체 재정의 필요
- Frontend/스크립트 호출부 전면 수정 필요
- 인프라(방화벽/프록시)에서 GET 전용 허용 정책 점검 필요

## 변경 방식(전환 전략)
### A안: 단일 릴리즈에서 일괄 전환(확정)
1. 기존 GET/PUT/DELETE 전부 POST로 변경
2. Controller 시그니처 변경 (RequestParam → RequestBody DTO)
3. 서비스/테스트 전면 수정
4. Swagger 문서/예제 전면 업데이트

### B안: 이중 지원(호환성 유지)
> 보류 (A안 확정으로 이번 전환 범위에는 포함하지 않음)

## 작업 단위(엔지니어링 기준)
1. **DTO 설계**
   - 기존 RequestParam 조합에 맞춰 Query DTO 정의
2. **Controller 수정**
   - `@GetMapping` → `@PostMapping`
   - `@PutMapping`/`@DeleteMapping` → `@PostMapping`
3. **SecurityConfig 정리**
   - 기존 Method 기반 권한 매핑 제거
   - 경로 기반 권한 매핑으로 변경
4. **테스트 수정**
   - `AuthControllerTests` 외 Config/Outbound 테스트 필요 시 추가
5. **문서/Swagger 예제 업데이트**

## 위험 요소
- 클라이언트 변경 누락 시 장애 발생 가능
- 모니터링/캐시/프록시 레이어에 영향
- GET 기반 로깅/방화벽 룰 재정의 필요

## 권장 순서(실행 체크리스트)
1. 대상 API 목록 확정 (본 문서 기준)
2. Query DTO 일괄 생성
3. Controller/Service 변경
4. SecurityConfig 권한 규칙 재설계
5. Swagger/문서 갱신
6. 통합 테스트/샘플 호출 갱신

## 결정 필요 항목
- **호환성 유지 기간**(A안 vs B안)
- **admin UI(/admin/**)도 POST 통일 대상인지 여부**
- **SSE 및 스트리밍 API**를 POST로 바꿀지 여부
  - SSE는 GET이 표준이며 POST 전환 시 클라이언트/프록시 호환성 리스크 큼

## 다음 단계 제안
- 필요 시 각 컨트롤러별 상세 매핑표(현재 GET/PUT/DELETE → POST) 산출 가능
- 호환성 전략 결정 후 실제 코드 변경 작업 착수

## 상세 매핑표(1차 초안)
> 기본 원칙: 기존 `@RequestParam`은 동일한 필드명으로 **request body JSON**에 옮깁니다.
> **Path 변수 노출 금지 정책 적용 시** `{id}`를 URL에서 제거하고 body로 이동합니다.
> **Body가 없는 조회 API는 GET 유지**하고, 요청/응답은 변경하지 않습니다.

### 공통/상태
- `ApiStatusController`
  - `GET /api/status` → **GET 유지** (body 없음)

### SCS SSE (주의)
- `ScsSseController`
  - `GET /api/v1/scs/app-status/stream` → **SSE 특성상 GET 유지 권장**
  - `GET /api/v1/scs/metrics` → **GET 유지** (body 없음)
  - `GET /api/v1/scs/app-status?dbid` → `POST /api/v1/scs/app-status` (body: `{ "dbid": 107 }`)

### Configuration API (/api/v1/configuration)
#### AgentGroupController
- `GET /agent-groups` → `POST /agent-groups` (body: `{ "tenantDbid": 1 }`)
- `POST /agent-groups`(생성) → `POST /agent-groups/create` (body: 기존 생성 DTO 유지)
- `GET /agent-groups/{groupDbid}` → `POST /agent-groups/get` (body: `{ "groupDbid": 10, "tenantDbid": 1 }`)
- `GET /agent-groups/by-name` → `POST /agent-groups/by-name` (body: `{ "name": "Sales", "tenantDbid": 1 }`)
- `PUT /agent-groups/{groupDbid}` → `POST /agent-groups/update` (body: `{ "groupDbid": 10, "payload": { /* 기존 update DTO */ } }`)
- `DELETE /agent-groups/{groupDbid}` → `POST /agent-groups/delete` (body: `{ "groupDbid": 10, "tenantDbid": 1 }`)

#### AgentLoginController
- `GET /agent-logins` → `POST /agent-logins` (body: `{ "tenantDbid": 1, "switchDbid": 1, "assignable": true }`)
- `POST /agent-logins`(생성) → `POST /agent-logins/create` (body: 기존 생성 DTO 유지)
- `GET /agent-logins/{loginCode}` → `POST /agent-logins/get` (body: `{ "loginCode": "1001", "tenantDbid": 1, "switchDbid": 1 }`)
- `PUT /agent-logins/{loginCode}` → `POST /agent-logins/update` (body: `{ "loginCode": "1001", "payload": { /* 기존 update DTO */ } }`)
- `DELETE /agent-logins/{loginCode}` → `POST /agent-logins/delete` (body: `{ "loginCode": "1001", "tenantDbid": 1, "switchDbid": 1 }`)

#### PersonController
- `GET /persons` → `POST /persons` (body: `{ "tenantDbid": 1 }`)
- `POST /persons`(생성) → `POST /persons/create` (body: 기존 생성 DTO 유지)
- `GET /persons/{personDbid}` → `POST /persons/get` (body: `{ "personDbid": 10, "tenantDbid": 1 }`)
- `GET /persons/by-employee` → `POST /persons/by-employee` (body: `{ "employeeId": "E001", "tenantDbid": 1 }`)
- `PUT /persons/{personDbid}` → `POST /persons/update` (body: `{ "personDbid": 10, "payload": { /* 기존 update DTO */ } }`)
- `DELETE /persons/{personDbid}` → `POST /persons/delete` (body: `{ "personDbid": 10, "tenantDbid": 1 }`)
- `DELETE /persons/{personDbid}/skills` → `POST /persons/skills/delete` (body: `{ "personDbid": 10, "tenantDbid": 1 }`)

#### RoutingConfigController (요약)
- DN/DNGroup/Transaction/Place/PlaceGroup 관련 모든 `GET` → `POST` 전환
  - 공통 body: `{ "tenantDbid": 1 }` + 조회 조건(예: `name`, `groupDbid` 등)
- 모든 `{id}` Path는 **body로 이동**
  - 예: `GET /transactions/{transactionDbid}` → `POST /transactions/get` (body: `{ "transactionDbid": 10001, "tenantDbid": 1 }`)
- 모든 `PUT` → `POST /.../update` (body에 `...Dbid` + 기존 update DTO payload 포함)
- 모든 `DELETE` → `POST /.../delete` (body에 `...Dbid` + `tenantDbid` 포함)
- 생성 API는 목록과 충돌 방지 위해 `POST /.../create`로 분리
- 트랜잭션 섹션/옵션 조작은 `/transactions/sections/*`, `/transactions/options/*`로 통일

#### OutboundConfigController (요약)
- `GET /calling-lists`, `/filters`, `/formats`, `/table-access`, `/treatment`, `/campaigns`, `/campaign-groups` 등
  → `POST` 전환, body에 `tenantDbid` 및 `name` 조건 포함
- 모든 `{id}` Path는 **body로 이동**
  - 예: `GET /campaigns/{campaignDbid}` → `POST /campaigns/get` (body: `{ "campaignDbid": 7001, "tenantDbid": 1 }`)
- 모든 `PUT` → `POST /.../update` (body에 `...Dbid` + 기존 update DTO payload 포함)
- 모든 `DELETE` → `POST /.../delete` (body에 `...Dbid` + `tenantDbid` 포함)
- 생성 API는 목록과 충돌 방지 위해 `POST /.../create`로 분리
- `POST /batch-create`의 `detail` 파라미터는 body로 통합

### Outbound API (/api/v1/outbound)
- `OutboundController`
  - `GET /health` → **GET 유지** (body 없음)

### Admin UI (/admin)
- 관리 UI는 브라우저 렌더링/폼 기반이므로 **POST 통일 대상에서 제외** 권장

## 보안/권한 매핑 영향
- `SecurityConfig`의 HTTP method 기반 권한 매핑은 제거 필요
- 경로 기반 권한 매핑으로 재정의(예: `/api/v1/configuration/**` 전체 권한)

## 혼합 파라미터(@RequestParam + @RequestBody) 처리 방안
현재 일부 API는 `@RequestParam`과 `@RequestBody`를 동시에 사용합니다. POST 통일 이후에는 **요청 구조를 단일 Body로 합치는 것을 기본 원칙**으로 합니다.

### 원칙
1. **Body로 일괄 통합**
   - 기존 Query Param은 Body DTO 필드로 이동
   - 기존 Body DTO는 Wrapper DTO로 감싸거나 필드를 상위로 승격
2. **경로(path variable)는 유지**
   - `{id}` 같은 path 변수는 그대로 유지하고, 나머지 입력은 body로 통합
3. **호환성 유지가 필요하면 과도기 지원**
   - 일정 기간 동안 `@RequestParam`도 선택적으로 허용하고, Body 우선 적용

### 예시 패턴
#### 기존 (혼합)
- `POST /agent-logins/{loginCode}`
  - `@RequestParam tenantDbid`, `@RequestParam switchDbid`
  - `@RequestBody AgentLoginUpdateRequest`

#### 변경 (통합)
- `POST /agent-logins/{loginCode}`
  - `@RequestBody AgentLoginUpdateCommand`
  - Body 예시:
    ```json
    {
      "tenantDbid": 1,
      "switchDbid": 1,
      "payload": { /* 기존 AgentLoginUpdateRequest 필드 */ }
    }
    ```

### DTO 설계 가이드
- `...Command` 또는 `...Request`로 통합 DTO 정의
- 단순 조회 요청이면 `...QueryRequest`로 통일
- 기존 Body DTO 재사용이 필요한 경우:
  - `payload` 필드로 감싸기 (가장 안전)
  - 또는 필드를 상위로 승격(중복 필드 충돌이 없을 때만)

### 혼합 파라미터 실제 목록(현 코드 기준)
- `OutboundConfigController#createOutboundBatch`
  - `POST /api/v1/configuration/batch-create`
  - `@RequestBody OutboundBatchCreateRequest` + `@RequestParam detail`
  - 통합안: `OutboundBatchCreateCommand { request, detail }`
- `AgentLoginController#updateAgentLogin`
  - `PUT /api/v1/configuration/agent-logins/{loginCode}`
  - `@RequestParam tenantDbid/switchDbid` + `@RequestBody AgentLoginUpdateRequest`
  - 통합안: `AgentLoginUpdateCommand { tenantDbid, switchDbid, payload }`

## URL 의미 은닉(기능 유추 방지) 로드맵
> 추후 작업으로 이관 (이번 A안 전환 범위에서는 제외)

## 문서/테스트 업데이트 기준
- Swagger: 모든 endpoint의 Method 및 RequestBody 스키마 갱신
- 테스트: 기존 GET/PUT/DELETE 기반 테스트 전면 수정
- 샘플 호출: `docs/works/api-post-migration-samples.http`에 최신 요청 예시 유지
