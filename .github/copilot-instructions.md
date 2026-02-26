# Copilot Instructions for Gen Outbound Gateway

## 핵심 아키텍처/패턴
- Spring Boot 4.0.1 + Java 17. 주요 패키지: `config`(보안/암호화/JWT/OpenAPI), `genesys`(Config/Outbound/Stat/T-Server 클라이언트), `web`(REST/Thymeleaf), `core`(공통 응답/예외).
- 공통 응답은 `core/ApiResponse` 사용, 예외는 `core/GlobalExceptionHandler`에서 `ApiException` + `GenesysUnavailableException` 매핑.
- Genesys 연동은 `genesys/*/service` 클라이언트가 담당: 
  - Config: `GenesysConfigClient`는 싱글톤 연결 + 헬스체크 + failover(primary→backup).
  - Outbound/Stat/T-Server: 요청 시 연결(per-request) + failover(primary→backup).
- CCC 암복호화는 `security/crypto/*`에서 처리. `@ConfigurationApiController` 또는 `@CccEncryptedController`가 붙은 컨트롤러는 요청에 `encData`만 허용하고 응답을 `{encData}`로 래핑.
- JWT 인증은 `security/*`에서 처리. `SecurityConfig`에서 `auth/jwt` 비활성화 시에는 `app.security.allow-insecure=true`가 필요.

## 개발 워크플로우/실행
- Genesys SDK는 `lib/`를 로컬 Maven에 설치해야 빌드됨: `scripts/install-genesys-sdk-local.ps1|.sh`. (프로필 `genesys-sdk`, `pom.xml`의 build-helper가 `src/genesys/java`를 소스에 추가)
- 실행 스크립트: 개발 `scripts/run-app.ps1`/`run-app.cmd`, 운영 JAR은 `scripts/run-jar.ps1|.cmd|.sh`.
- 로컬은 `SPRING_PROFILES_ACTIVE=local`에서 H2 사용. 설정 예시는 `config/application-*.yml` 및 `config/.env*.example` 참고.
- 검증 시 `mvn verify`로 Checkstyle/SpotBugs까지 수행(플러그인 설정은 `pom.xml`).

## 프로젝트 특화 규칙/통합 포인트
- Genesys 연결 상태는 `/api/status`에서 통합 확인(`ApiStatusController`), 클라이언트별 `getConnectionStatus()`를 사용.
- Outbound/Stat/T-Server API는 각각 `genesys/*/web` 컨트롤러에 위치하며 Swagger 예제가 풍부함.
- 인증 사용자 저장은 `security/AppUserRepository` + `app_users` 테이블. `SecurityProperties`에 `ADMIN_USERNAME`/`ADMIN_PASSWORD`가 있으면 DB 대신 관리자 계정 우선 사용.
- 외부 연동 오류는 `GenesysUnavailableException`으로 통일해 503 응답을 반환.

## 문서/참고 위치
- 운영/개발 설정 및 엔드포인트 요약은 `README.md`.
- 변경 기록은 `docs/works/`, 의사결정은 `adr/`.

## 안전/주의 사항
- 시크릿(`JWT_SECRET`, `CCC_SERVICE_ENC_KEY/IV`)은 반드시 환경변수로 주입하며 리포지토리에 커밋하지 않음.
- 암복호화가 활성화된 컨트롤러는 평문 JSON을 받지 않으므로, 테스트 요청도 `encData` 포맷을 유지.
