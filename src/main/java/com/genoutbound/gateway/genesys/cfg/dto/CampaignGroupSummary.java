package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

public record CampaignGroupSummary(
        @Schema(description = "CampaignGroup DBID", example = "6001")
        int dbid,
        @Schema(description = "CampaignGroup 이름", example = "GROUP_A")
        String name,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled,
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "캠페인 DBID", example = "7001")
        Integer campaignDbid,
        @Schema(description = "그룹 DBID", example = "8001")
        Integer groupDbid,
        @Schema(description = "그룹 타입", example = "CFGGroup")
        String groupType,
        @Schema(description = "설명", example = "그룹 설명")
        String description,
        @Schema(description = "상태", example = "CFGEnabled")
        String state,
        @Schema(description = "다이얼 모드", example = "Predictive")
        String dialMode,
        @Schema(description = "운영 모드", example = "Basic")
        String operationMode,
        @Schema(description = "채널 수", example = "30")
        Integer numOfChannels,
        @Schema(description = "최적화 방법", example = "Availability")
        String optMethod,
        @Schema(description = "최적화 값", example = "0")
        Integer optMethodValue,
        @Schema(description = "최소 녹취 버퍼 크기", example = "0")
        Integer minRecBuffSize,
        @Schema(description = "권장 녹취 버퍼 크기", example = "0")
        Integer optRecBuffSize,
        @Schema(description = "Orig DN DBID", example = "9001")
        Integer origDnDbid,
        @Schema(description = "TrunkGroup DN DBID", example = "9002")
        Integer trunkGroupDnDbid,
        @Schema(description = "스크립트 DBID", example = "2001")
        Integer scriptDbid,
        @Schema(description = "Interaction Queue DBID", example = "2101")
        Integer interactionQueueDbid,
        @Schema(description = "IVR Profile DBID", example = "2201")
        Integer ivrProfileDbid,
        @Schema(description = "서버 목록", example = "[{\"dbid\":3001,\"name\":\"OCS_A\"}]")
        List<ServerSummary> servers,
        @Schema(description = "Orig DN 번호", example = "1001")
        String origDnNumber,
        @Schema(description = "TrunkGroup DN 번호", example = "9000")
        String trunkGroupDnNumber,
        @Schema(description = "사용자 속성")
        Map<String, Object> userProperties
) {
}
