package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CampaignUpdateCommand(
    @Schema(description = "캠페인 DBID", example = "7001")
    int campaignDbid,
    @Schema(description = "캠페인 수정 요청")
    CampaignRequest payload
) {
}