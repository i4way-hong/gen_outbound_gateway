package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

public record CampaignGroupRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "캠페인 DBID", example = "7001")
    Integer campaignDbid,
    @Schema(description = "그룹 DBID", example = "8001")
    Integer groupDbid,
    @Schema(description = "그룹 타입(CfgObjectType)", example = "CFGGroup")
    String groupType,
    @Schema(description = "CampaignGroup 이름", example = "GROUP_A")
    String name,
    @Schema(description = "설명", example = "그룹 설명")
    String description,
    @Schema(description = "다이얼 모드(CfgDialMode)", example = "Predictive")
    String dialMode,
    @Schema(description = "운영 모드(CfgOperationMode)", example = "Basic")
    String operationMode,
    @Schema(description = "채널 수", example = "30")
    Integer numOfChannels,
    @Schema(description = "최적화 방법(CfgOptimizationMethod)", example = "Availability")
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
    @Schema(description = "OCS/Outbound 서버 DBID 목록", example = "[3001,3002]")
    List<Integer> serverDbids,
    @Schema(
        description = "사용자 속성 (예: OCServer/CampaignType)",
        example = "{\"OCServer\":{\"CampaignType\":\"VOICE\"}}"
    )
    Map<String, Map<String, String>> userProperties,
    @Schema(description = "활성화 여부", example = "true")
    boolean enabled
) {
}
