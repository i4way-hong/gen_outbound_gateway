package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OutboundBatchCreateResponse(
    @Schema(description = "생성된 Filter")
    FilterSummary filter,
    @Schema(description = "생성된 CallingList")
    CallingListDetailSummary callingList,
    @Schema(description = "생성된 Campaign")
    CampaignSummary campaign,
    @Schema(description = "생성된 CampaignGroup")
    CampaignGroupSummary campaignGroup
) {
}
