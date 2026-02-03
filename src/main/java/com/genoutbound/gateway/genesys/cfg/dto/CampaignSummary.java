package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

public record CampaignSummary(
        @Schema(description = "캠페인 DBID", example = "7001")
        int dbid,
        @Schema(description = "캠페인 이름", example = "CMP_A")
        String name,
        @Schema(description = "설명", example = "캠페인 설명")
        String description,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled,
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스크립트 DBID", example = "2001")
        Integer scriptDbid,
        @Schema(description = "캠페인 상태", example = "CFGEnabled")
        String state,
        @Schema(description = "콜링리스트 DBID 목록", example = "[3001,3002]")
        List<CallingListDetailSummary> callingLists,
        @Schema(description = "사용자 속성")
        Map<String, Object> userProperties,
        @Schema(description = "캠페인 그룹 상세 목록")
        List<CampaignGroupSummary> campaignGroups
) {
}
