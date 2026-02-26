# SCS Application 상태 변경 → SSE 전파 설계 메모 (2026-02-25)

## 목적
- Genesys Solution Control Server(SCS)에서 Application 상태 변경 이벤트를 수신하고, 서버 내 상태를 갱신한 뒤 SSE(Server-Sent Events)로 구독 중인 클라이언트에 전달하는 기능 설계.

## 참고 소스 요약
### `docs/참고소스/sample/ScsReciver.java`
- `SolutionControlServerProtocol` 기반 SCS 연결.
- `WarmStandbyService` + `WarmStandbyListener`로 Primary/Backup 전환 처리.
- `ChannelListener`로 채널 open/close/error 처리.
- `RequestSubscribe`로 Application(DBID) 상태 변경 구독.
- 수신 메시지: `EventInfo` (messageId == `EventInfo.ID`).

### API 문서 핵심 (docs/참고소스/doc/api)
- `RequestSubscribe`:
  - 설명: “상태 변경 알림 이벤트 구독” (`Subscription request. Subscribes to notification events for configuration objects that receive a status change.`)
  - 핵심 필드: `controlObjectType`, `controlObjectId` (DBID)
- `EventInfo`:
  - 설명: “Status information event.”
  - 핵심 필드/메서드
    - `getControlObjectType()` → `ControlObjectType` (예: `Application`)
    - `getControlObjectId()` → DBID
    - `getControlStatus()` → 상태 코드 (정수)
    - `getExecutionMode()` → `ApplicationExecutionMode` (`Primary`, `Backup`, `Exiting`)
    - `getDescription()` → 상태/설명 문자열
- `ApplicationStatus`:
  - 주요 상태: `Unknown`, `Stopped`, `StopTransition`, `StopPending`, `StartTransition`, `StartPending`, `Running`, `Initializing`, `Suspending`, `Suspended`, `ServiceUnavailable`
  - 실제 메시지에 들어오는 `controlStatus`는 ApplicationStatus의 정수 값과 매핑되는 것으로 해석 (SDK에서는 `GEnum` 기반)

## 설계 개요
### 입력/출력 계약(초안)
- 입력: SCS `EventInfo` (Application 상태 변경)
- 출력: SSE 이벤트
  - `event`: `app-status`
  - `data`: JSON (예: 아래)

```json
{
  "appDbid": 12345,
  "appName": "MyApp",
  "controlStatus": 5,
  "status": "Running",
  "executionMode": "Primary",
  "description": "Running",
  "receivedAt": "2026-02-25T10:32:15.123+09:00"
}
```

## 처리 흐름 (권장)
1. **SCS 연결/수신**
   - `SolutionControlServerProtocol` 생성 후 `WarmStandbyService` 적용.
   - `MessageHandler`에서 `EventInfo` 수신 처리.
2. **구독 등록**
   - 대상 Application(DBID) 목록에 대해 `RequestSubscribe` 전송.
   - `controlObjectType = ControlObjectType.Application`
3. **도메인 이벤트 변환**
   - `EventInfo.getControlStatus()` → `ApplicationStatus.getValue(...)`로 상태 문자열 계산.
   - 필요 시 DBID → AppName 매핑(설정 캐시/DB/Config API 등).
4. **SSE 전파**
   - 구독 중인 `SseEmitter` 목록에 broadcast.
   - 전송 실패 emitter 제거.

## 구현 스케치 (Spring Boot 기준)
### 1) SCS 수신 서비스
- 위치 제안: `genesys/scs/service/ScsEventService`
- 역할
  - 프로토콜 연결, warm-standby 이벤트 처리
  - `RequestSubscribe` 전송
  - `EventInfo` 수신 시 내부 이벤트 publish

### 2) SSE 브로드캐스트 서비스
- 위치 제안: `web/sse/AppStatusSseService`
- 책임
  - `SseEmitter` 등록/해제
  - heartbeat (예: 25~30초)로 연결 유지
  - `broadcast(AppStatusEvent evt)`

### 3) SSE 컨트롤러
- 위치 제안: `web/sse/AppStatusSseController`
- 엔드포인트 예시: `GET /api/sse/app-status`
- 반환: `SseEmitter`

> 주의: `@CccEncryptedController` 대상에 SSE를 올리는 경우 SSE 포맷이 깨질 수 있으므로, SSE는 일반 JSON 채널로 별도 제공 권장.

## 상태 매핑 제안
- `controlStatus`(int) → `ApplicationStatus`로 매핑
- 방법
  - `ApplicationStatus.getValue(ApplicationStatus.class, controlStatus)` 사용
  - 또는 `ApplicationStatus.getValue(ApplicationStatus.class, Integer)`

## 장애/엣지 케이스
- SCS 채널 끊김/오류 → warm-standby 전환 이벤트 로깅 + 재구독 필요 여부 판단
- EventInfo가 Application이 아닌 경우(Host/Solution 등) → 필터링
- SSE 클라이언트 연결이 많을 때: emitter 갯수 제한 or 만료 정책
- 장시간 무활동 연결 → heartbeat 이벤트로 keep-alive
- 상태 중복 이벤트 → 최근 상태 캐시로 중복 전송 최소화(선택)

## 로그/모니터링 포인트
- warm-standby 상태 변경 (`onWarmStandbyStateChanged`)
- 채널 open/close/error
- `EventInfo` 수신 로그 (App DBID, Status, ExecutionMode)
- SSE emitter 수/브로드캐스트 실패 카운트

## 다음 단계 제안
1. Application DBID/이름 소스 결정 (Config API or DB)
2. `genesys/scs/service` 모듈 추가 및 프로토콜 라이프사이클 정의
3. SSE 엔드포인트 설계 + 권한 정책 결정
4. 통합 테스트: 상태 변경 이벤트 모킹 → SSE 수신 확인
