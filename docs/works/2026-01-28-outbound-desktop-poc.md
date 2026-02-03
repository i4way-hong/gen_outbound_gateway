# Outbound Desktop POC (2026-01-28)

## 개요
- Outbound Desktop 메시지(AddRecord, AddRecordAcknowledge, DoNotCall, DoNotCallAcknowledge) KV 마샬링 POC
- T-Server 전송 경로(RequestDistributeUserEvent) 추가

## 주요 변경
- `/api/v1/outbound/desktop/*` POC 엔드포인트 추가
- T-Server 연동 설정(`app.outbound.desktop.*`) 및 전송 메서드 추가
- EventUserEvent 수신 리스너 및 `/events`, `/events/clear` 엔드포인트 추가
- `/events` 조회에 messageType/userEventId/limit 필터 추가

## 비고
- 전송/수신 엔드포인트는 T-Server 설정이 필요합니다.
- 수신 버퍼는 `app.outbound.desktop.event-buffer-size`로 조정합니다.
