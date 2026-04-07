/**
 * 공통 설정 패키지.
 *
 * <p>클래스 역할:
 * <ul>
 *   <li>{@code SecurityConfig}: Spring Security 인가/인증 필터 체인 구성</li>
 *   <li>{@code SecurityProperties}: 보안 관련 외부 설정 프로퍼티 바인딩</li>
 *   <li>{@code JwtConfig}: JWT 관련 빈/환경 구성</li>
 *   <li>{@code JwtProperties}: JWT 서명/만료시간 설정값 바인딩</li>
 *   <li>{@code EncryptionConfig}: CCC 암복호화 기능 활성화/빈 구성</li>
 *   <li>{@code EncryptionProperties}: 암복호화 키/IV 설정값 바인딩</li>
 *   <li>{@code OpenApiConfig}: Swagger/OpenAPI 문서 노출 범위 및 메타데이터 구성</li>
 *   <li>{@code OpenApiSecurityConfig}: OpenAPI 문서의 보안 스키마 구성</li>
 *   <li>{@code SchedulingConfig}: 스케줄링 기능 활성화 및 스레드 정책 구성</li>
 *   <li>{@code JacksonConfig}: JSON 직렬화/역직렬화 ObjectMapper 커스터마이징</li>
 *   <li>{@code ConfigurationApiBlockFilter}: 특정 API 접근 제어를 위한 서블릿 필터</li>
 * </ul>
 */
package com.genoutbound.gateway.config;
