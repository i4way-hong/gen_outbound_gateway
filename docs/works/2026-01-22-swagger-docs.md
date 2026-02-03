# 2026-01-22 Swagger 문서 보강

## 작업 요약
- Configuration API 컨트롤러들(Person/AgentGroup/AgentLogin/OutboundConfig/Routing)과 `OutboundController`에 Swagger API/파라미터 설명 추가.
- 공통 에러 응답(400/409/500/503) 문서화.
- 주요 요청/응답 DTO에 `@Schema` 설명 및 예시 추가.
- `ApiResponse`에 공통 응답 스키마 설명 추가.
- 콜링리스트 상세 요청 DTO는 `CallingListDetailRequest`로 표기.

## 변경 파일
- `src/main/java/com/genoutbound/gateway/core/ApiResponse.java`
- `src/main/java/com/genoutbound/gateway/genesys/outbound/web/OutboundController.java`
- `src/main/java/com/genoutbound/gateway/genesys/cfg/web/ConfigurationApiController.java`
- `src/main/java/com/genoutbound/gateway/genesys/cfg/web/*Controller.java`
- `src/main/java/com/genoutbound/gateway/genesys/outbound/dto/*`
- `src/main/java/com/genoutbound/gateway/genesys/cfg/dto/*`

## 확인
- `cmd /c mvn -q test`
