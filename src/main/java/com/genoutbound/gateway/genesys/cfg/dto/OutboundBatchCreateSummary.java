package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OutboundBatchCreateSummary(
    @Schema(description = "생성된 Filter 요약")
    IdName filter,
    @Schema(description = "생성된 CallingList 요약")
    IdName callingList,
    @Schema(description = "생성된 Campaign 요약")
    IdName campaign,
    @Schema(description = "생성된 CampaignGroup 요약")
    IdName campaignGroup
) {

    public record IdName(
        @Schema(description = "DBID", example = "7001")
        int dbid,
        @Schema(description = "이름", example = "CMP_A")
        String name
    ) {
    }
}
