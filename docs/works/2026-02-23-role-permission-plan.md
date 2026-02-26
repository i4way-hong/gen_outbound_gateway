# 역할 기반 권한관리(UI 전용) 구현 계획

작성일: 2026-02-23

## 목표
- 운영자용 UI에서 사용자/역할(Role)/권한(Permission)을 관리
- API 접근 제어는 Role 기반으로 중앙 관리
- 기존 `app_users.roles`와 JWT 흐름을 유지하면서 확장

## 범위/원칙
- **Role 중심**으로 권한 매핑 (사용자↔역할↔권한)
- **UI 전용** 관리 페이지 제공 (REST API는 내부에서만 사용)
- 기존 패턴(`ApiResponse`, `GlobalExceptionHandler`, `SecurityConfig`) 유지

## 전체 단계 요약
1) **데이터 모델 확정 및 설계 문서화** (Role/Permission 스키마, 매핑 규칙)
2) JPA 엔티티/리포지토리 추가 + 마이그레이션 스크립트
3) 관리자 UI(Thymeleaf) 화면/컨트롤러 구현
4) Security 권한 매핑(`@PreAuthorize` 또는 `requestMatchers`) 적용
5) 운영자 시나리오 테스트 및 문서 업데이트

---

## 1단계 상세: 데이터 모델 확정 및 설계 문서화

### 1-1. 권한 체계 정의
- 권한 문자열 prefix: `PERM_` (예: `PERM_OUTBOUND_WRITE`)
- 역할(Role)은 `ROLE_` prefix 유지 (Spring Security 규칙)
- API 그룹 단위 권한 제안
  - Outbound: `PERM_OUTBOUND_READ`, `PERM_OUTBOUND_WRITE`
  - Config: `PERM_CONFIG_READ`, `PERM_CONFIG_WRITE`
  - Stat/T-Server: `PERM_STAT_READ`, `PERM_TSERVER_WRITE`
  - 공통: `PERM_STATUS_READ`, `PERM_ADMIN_UI`

### 1-2. 스키마 초안
- `app_roles`
  - `id` (PK)
  - `name` (unique, 예: `ADMIN`, `OPERATOR`)
  - `description`
  - `enabled`
- `app_permissions`
  - `id` (PK)
  - `code` (unique, 예: `PERM_OUTBOUND_WRITE`)
  - `description`
- `app_role_permissions`
  - `role_id` (FK → `app_roles`)
  - `permission_id` (FK → `app_permissions`)
- `app_user_roles`
  - `user_id` (FK → `app_users`)
  - `role_id` (FK → `app_roles`)

> 기존 `app_users.roles`는 유지하되, **마이그레이션 이후 읽기 전용**으로 두고 신규 화면에서는 `app_user_roles` 기준으로 관리.

### 1-3. 권한 해석 규칙
- 로그인 시 `UserDetailsService`에서:
  1) `app_user_roles` → `app_roles`
  2) 역할에 매핑된 `app_permissions`
  3) 최종 `GrantedAuthority`: `ROLE_*` + `PERM_*`
- 기존 `ADMIN_USERNAME`/`ADMIN_PASSWORD`가 설정된 경우:
  - 관리자 계정에 `ROLE_ADMIN` + 모든 `PERM_*` 부여

### 1-4. 필요한 코드 변경 지점 (구체화)
- `security/DatabaseUserDetailsService`
  - DB에서 Role/Permission 조합을 읽도록 확장
- 신규 엔티티/리포지토리 (예시 위치)
  - `security/role/AppRole`, `security/role/AppPermission`
  - `security/role/AppRoleRepository`, `security/role/AppPermissionRepository`
- 권한 상수 정의 (예: `security/permission/PermissionCodes`)
- 시드 플래그(`app.security.seed-enabled`)로 기본 권한/역할 초기화
- `ADMIN_USERNAME`/`ADMIN_PASSWORD` 설정 시 관리자 계정 자동 생성

### 1-5. 결정 필요 사항
- 기본 Role 세트: `ADMIN`, `OPERATOR` 외 추가 여부
- 초기 권한 시드(bootstrap) 위치: `CommandLineRunner` or SQL 스크립트
- 기존 `app_users.roles` 데이터 이관 여부

---

## 다음 단계 진행 제안
- 위 스키마/권한 체계가 승인되면 2단계(JPA + 마이그레이션)부터 구현 착수
