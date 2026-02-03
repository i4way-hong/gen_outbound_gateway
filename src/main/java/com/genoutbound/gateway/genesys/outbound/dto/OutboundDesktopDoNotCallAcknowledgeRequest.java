package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public record OutboundDesktopDoNotCallAcknowledgeRequest(
        @Schema(description = "Application ID", example = "1001")
        Integer applicationId,
        @Schema(description = "캠페인 이름", example = "CMP_A")
        String campaignName,
        @Schema(description = "콜링리스트 이름", example = "LIST_A")
        String callingList,
        @Schema(description = "T-Server 통신 DN", example = "3001")
        String communicationDn,
        @Schema(description = "ChainAttribute 값", example = "AllChain")
        String chainAttr,
        @Schema(description = "레코드 핸들", example = "123")
        Integer recordHandle,
        @Schema(description = "레코드 전화번호", example = "01012341234")
        String phone,
        @Schema(description = "추가 필드(Map)")
        Map<String, String> otherFields
) {
}
