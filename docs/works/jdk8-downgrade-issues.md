# JDK 1.8 다운그레이드 시 예상 문제점 (현재 기준)

## 대상 범위
- 리포지토리: `Gen_Outbound_Gateway`
- 현재 설정: Spring Boot 4.0.1, Java 17 (`pom.xml`의 `java.version=17`)
- 기준 시점: 2026-03-09

## 핵심 결론
이 프로젝트는 **JDK 17 전제**로 구성되어 있어, **JDK 1.8로 내리면 빌드/실행이 불가능**합니다. 
주요 원인은 **Spring Boot 4.x의 Java 17 요구사항**과 **코드 레벨의 Java 17/11/9 문법 및 API 사용**입니다.

## 1) 프레임워크/빌드 레벨 차단
### Spring Boot 4.x 요구사항
- `spring-boot-starter-parent 4.0.1`은 **Java 17 이상**이 필수입니다.
- JDK 1.8에서는 **컴파일 자체가 불가**합니다.

### Spring Security / Spring Framework 6
- Spring Framework 6 기반으로 **Jakarta EE 9+ (`jakarta.*`) 네임스페이스**를 사용합니다.
- JDK 8 + javax 기반 생태계와 호환이 제한적이며, 다운그레이드 시 **대규모 의존성 교체**가 필요합니다.

## 2) 코드 레벨 호환성 문제 (Java 8 미지원 문법/SDK)
프로젝트 코드에 **Java 9+ / 14+ / 16+ 문법**이 다수 포함되어 있어 JDK 8에서 컴파일 오류가 발생합니다.

### Java `record` 사용 (Java 16+)
- 예시: `AuthRequest`, `RefreshRequest`, `TokenResponse`, `LogoutRequest`, `ScsAppStatusRequest` 등 다수
- JDK 8에서는 `record` 문법이 없어 **전량 클래스 변경 필요**

### Switch Expression (Java 14+)
- 예시: `OutboundService`의 `switch` expression 사용
- JDK 8에서는 `switch` expression 문법 지원 없음 → **기존 switch 문으로 재작성 필요**

### 컬렉션 팩토리 메서드 (Java 9+)
- 예시: `List.of(...)`, `Map.of(...)`, `Set.of(...)` 다수 사용
- JDK 8에서는 존재하지 않음 → **Collections.unmodifiableList/Map 등으로 대체 필요**

## 3) 의존성/라이브러리 호환성 위험
다음 라이브러리는 JDK 8 미지원이거나, Boot 4.x와 함께 사용하도록 설계되었습니다.

- `springdoc-openapi-starter-webmvc-ui 2.8.0` (Java 17 전제)
- Spring Boot 4.x / Spring Security 6.x 전반
- Jakarta 기반 `jakarta.validation`, `jakarta.servlet` 등

**결론:** JDK 8로 내리려면 **의존성 버전 일괄 다운그레이드 + 코드 대규모 수정**이 필요합니다.

## 4) 예상되는 실제 실패 증상
- `java.version=8`으로 변경 시 Maven 빌드 단계에서 **즉시 컴파일 실패**
- `record`, `switch expression`, `List.of` 등으로 **컴파일 오류 다수**
- Spring Boot 4.x 관련 플러그인/의존성에서 **UnsupportedClassVersionError** 발생 가능

## 5) 다운그레이드 시 필요한 대규모 작업(요약)
1. **Spring Boot 4.x → 2.7.x 또는 2.6.x로 다운그레이드**
2. `jakarta.*` → `javax.*` 네임스페이스로 전환
3. 모든 `record` 클래스 → 일반 POJO 변환
4. `switch expression` → 기존 switch 문으로 재작성
5. `List.of/Map.of/Set.of` → `Collections.unmodifiable*` 대체
6. springdoc 2.x → 1.x 또는 Springfox로 변경

## 6) JDK 8 대응 작업 로드맵
### Phase 0. 전제 정리 (1~2일)
- 목표 Java 버전: 1.8 (HotSpot) 확정
- 호환 프레임워크 선택: Spring Boot 2.7.x (마지막 Java 8 지원 라인)
- 빌드/배포 파이프라인에서 Java 8 사용 가능 여부 확인

### Phase 1. 프레임워크 다운그레이드 (3~5일)
- `spring-boot-starter-parent`를 2.7.x로 변경
- Spring Framework 6 → 5.3.x 계열로 전환
- `jakarta.*` → `javax.*` 네임스페이스로 전면 교체
- `springdoc-openapi-starter` → `springdoc-openapi-ui` 1.6.x 또는 Springfox로 전환

### Phase 2. 코드 레벨 변환 (5~10일)
- `record` 제거 및 POJO 변환
- `switch expression` 제거
- `List.of/Map.of/Set.of` 제거
- 컴파일 실패 지점 순차 해결

### Phase 3. 기능 회귀 테스트 및 운영 검증 (3~7일)
- 핵심 API 회귀 테스트 (Auth, Config, Outbound, Stat, Tserver)
- 암복호화/보안 플로우 검증
- 운영 환경 스모크 테스트

## 7) 파일별 변환 체크리스트 (대표/고위험 위주)
### A. `record` → POJO 전환 대상
- `src/main/java/com/genoutbound/gateway/security/dto/AuthRequest.java`
- `src/main/java/com/genoutbound/gateway/security/dto/RefreshRequest.java`
- `src/main/java/com/genoutbound/gateway/security/dto/TokenResponse.java`
- `src/main/java/com/genoutbound/gateway/security/dto/LogoutRequest.java`
- `src/main/java/com/genoutbound/gateway/web/dto/ScsAppStatusRequest.java`
- `src/main/java/com/genoutbound/gateway/sse/AppStatusEvent.java`
- `src/main/java/com/genoutbound/gateway/sse/AppStatusSseService.java` (내부 `EmitterInfo` record)
- `src/main/java/com/genoutbound/gateway/security/crypto/web/CryptoTestController.java` (내부 record)
- `src/main/java/com/genoutbound/gateway/security/crypto/web/CryptoSecureEchoController.java` (내부 record)
- `src/main/java/com/genoutbound/gateway/genesys/cfg/dto/*` (다수 record)

### B. `switch expression` 제거 대상
- `src/main/java/com/genoutbound/gateway/genesys/outbound/service/OutboundService.java`
- `src/main/java/com/genoutbound/gateway/genesys/tserver/web/TserverController.java` (switch 사용 구간 점검)

### C. `List.of/Map.of/Set.of` 제거 대상 (전반)
- `src/main/java/com/genoutbound/gateway/**` 다수 (예: `OutboundConfigService`, `ScsEventService`, `AppStatusSseService`)
- 방법: `Collections.unmodifiableList(Arrays.asList(...))` 등으로 치환

### D. Jakarta → javax 전환 대상(패키지 전수)
- `jakarta.validation.*`, `jakarta.servlet.*` 등 사용 파일 전반
- 예: `CryptoTestController`, `CryptoSecureEchoController`, 각 컨트롤러/DTO

## 8) 체크리스트 (실행 순서 제안)
1. `pom.xml`에서 Boot 버전 변경 + `java.version=8`
2. `jakarta.*` → `javax.*` 전환 (일괄 리팩터)
3. record 변환(컴파일 오류 제거 1순위)
4. switch expression 변환
5. `List.of/Map.of/Set.of` 치환
6. springdoc 버전 전환 및 Swagger 설정 재검증
7. 전체 컴파일 및 테스트 수행

## 부록: 근거 (현재 코드 확인 결과)
- `pom.xml` → `java.version=17` 및 Spring Boot 4.0.1
- `record` 사용 다수: `security/dto`, `genesys/cfg/dto`, `sse`, `web/dto` 등
- `switch expression` 사용: `OutboundService`
- `List.of/Map.of/Set.of` 사용 다수

---

필요하시면 **파일별 상세 변환 샘플**(record → POJO, switch → if/switch)도 추가로 정리해 드릴게요.
