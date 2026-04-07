/**
 * API 공통 응답/예외 처리 패키지.
 *
 * <p>클래스 역할:
 * <ul>
 *   <li>{@code ApiResponse}: 전역 표준 응답 포맷(record)</li>
 *   <li>{@code ApiException}: 도메인/비즈니스 오류를 표현하는 커스텀 예외</li>
 *   <li>{@code GlobalExceptionHandler}: 컨트롤러 예외를 HTTP 응답으로 변환하는 전역 핸들러</li>
 * </ul>
 */
package com.genoutbound.gateway.core;
