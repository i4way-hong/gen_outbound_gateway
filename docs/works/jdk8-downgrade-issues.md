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

## 부록: 근거 (현재 코드 확인 결과)
- `pom.xml` → `java.version=17` 및 Spring Boot 4.0.1
- `record` 사용 다수: `security/dto`, `genesys/cfg/dto`, `sse`, `web/dto` 등
- `switch expression` 사용: `OutboundService`
- `List.of/Map.of/Set.of` 사용 다수

---

필요하시면 **JDK 8 대응 로드맵(작업 분해 + 리스크)**도 정리해 드릴게요.
