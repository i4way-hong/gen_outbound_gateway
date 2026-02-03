package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OutboundStatusResponse(
        @Schema(description = "캠페인 DBID", example = "7001")
        int campaignDbid,
        @Schema(description = "그룹 DBID", example = "6001")
        int groupDbid,
        @Schema(description = "상태", example = "OK")
        String status
) {
}
