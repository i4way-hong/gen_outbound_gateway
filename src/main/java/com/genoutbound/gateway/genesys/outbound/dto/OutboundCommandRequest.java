package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OutboundCommandRequest(
        @Schema(description = "캠페인 DBID", example = "7001")
        @NotNull @Min(1) Integer campaignDbid,
        @Schema(description = "그룹 DBID", example = "6001")
        @NotNull @Min(1) Integer groupDbid
) {
}
