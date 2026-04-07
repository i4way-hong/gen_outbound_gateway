/**
 * Genesys Config Server 비즈니스 서비스 패키지.
 *
 * <p>클래스 역할:
 * <ul>
 *   <li>{@code GenesysConfigClient}: Config Server 연결 생성/유지/헬스체크/Failover 담당</li>
 *   <li>{@code GenesysConfigSupport}: Config 객체 조회/검증 공통 유틸리티 서비스</li>
 *   <li>{@code AgentGroupConfigService}: Agent Group 조회/생성/수정/삭제 로직</li>
 *   <li>{@code AgentLoginConfigService}: Agent Login 조회/생성/수정/삭제 로직</li>
 *   <li>{@code PersonConfigService}: Person 조회/생성/수정/삭제 및 연관 설정 로직</li>
 *   <li>{@code RoutingConfigService}: Routing 관련 Config 객체 조회/변경 로직</li>
 *   <li>{@code OutboundConfigService}: Outbound 캠페인/리스트/필터/트랜잭션 등 Config 로직</li>
 * </ul>
 */
package com.genoutbound.gateway.genesys.cfg.service;
