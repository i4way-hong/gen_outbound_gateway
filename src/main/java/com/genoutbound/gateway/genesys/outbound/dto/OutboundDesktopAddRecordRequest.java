package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public record OutboundDesktopAddRecordRequest(
        @Schema(description = "Application ID", example = "1001")
        Integer applicationId,
        @Schema(description = "캠페인 이름", example = "CMP_A")
        String campaignName,
        @Schema(description = "콜링리스트 이름", example = "LIST_A")
        String callingList,
        @Schema(description = "T-Server 통신 DN", example = "3001")
        String communicationDn,
        @Schema(description = "레코드 전화번호", example = "01012341234")
        String phone,
        @Schema(description = "Agent ID", example = "AGENT01")
        String agentId,
        @Schema(description = "시도 횟수", example = "0")
        Integer attempts,
        @Schema(description = "CallResult 코드", example = "1")
        Integer callResultCode,
        @Schema(description = "콜 시간(초)", example = "120")
        Integer callTime,
        @Schema(description = "레코드 생성 일시(ISO-8601)", example = "2026-01-28T09:00:00+09:00")
        String dateTime,
        @Schema(description = "콜 허용 시작 시간(HHmm)", example = "900")
        Integer from,
        @Schema(description = "콜 허용 종료 시간(HHmm)", example = "1800")
        Integer until,
        @Schema(description = "타임존 이름", example = "Asia/Seoul")
        String tzName,
        @Schema(description = "PhoneType 값", example = "Business")
        String phoneType,
        @Schema(description = "RecordStatus 값", example = "Ready")
        String recordStatus,
        @Schema(description = "RecordType 값", example = "Regular")
        String recordType,
        @Schema(description = "추가 필드(Map)")
        Map<String, String> otherFields
) {
}
