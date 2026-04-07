/**
 * 인증/인가 핵심 도메인 패키지.
 *
 * <p>클래스 역할:
 * <ul>
 *   <li>{@code AppUser}: 애플리케이션 사용자 엔티티</li>
 *   <li>{@code AppUserRepository}: 사용자 저장소(JPA Repository)</li>
 *   <li>{@code AuthService}: 로그인/토큰 재발급/로그아웃 비즈니스 처리</li>
 *   <li>{@code DatabaseUserDetailsService}: Spring Security 사용자 조회 어댑터</li>
 *   <li>{@code JwtAuthenticationFilter}: 요청 JWT 검증 및 SecurityContext 설정 필터</li>
 *   <li>{@code JwtTokenProvider}: JWT 생성/검증/파싱 유틸리티</li>
 *   <li>{@code PasswordHashGenerator}: 비밀번호 해시 생성 보조 유틸리티</li>
 *   <li>{@code TokenRevocationService}: 로그아웃 토큰 폐기(블랙리스트) 처리</li>
 *   <li>{@code TokenVersionService}: 토큰 버전 기반 무효화 관리</li>
 * </ul>
 */
package com.genoutbound.gateway.security;
