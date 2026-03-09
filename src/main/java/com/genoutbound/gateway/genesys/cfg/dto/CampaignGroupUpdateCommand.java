package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CampaignGroupUpdateCommand(
    @Schema(description = "CampaignGroup DBID", example = "6001")
    int groupDbid,
    @Schema(description = "CampaignGroup 수정 요청")
    CampaignGroupRequest payload
) {
}