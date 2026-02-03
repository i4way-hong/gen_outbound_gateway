package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record OutboundBatchCreateRequest(
    @Schema(description = "Filter 생성 요청")
    @NotNull @Valid FilterRequest filter,
    @Schema(description = "콜링리스트 생성 요청")
    @NotNull @Valid CallingListDetailRequest callingList,
    @Schema(description = "캠페인 생성 요청")
    @NotNull @Valid CampaignRequest campaign,
    @Schema(description = "CampaignGroup 생성 요청")
    @NotNull @Valid CampaignGroupRequest campaignGroup
) {
}
