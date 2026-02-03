package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OutboundDialRequest(
    @Schema(description = "캠페인 DBID", example = "7001")
    @NotNull @Min(1) Integer campaignDbid,
    @Schema(description = "그룹 DBID", example = "6001")
    @NotNull @Min(1) Integer groupDbid,
    @Schema(description = "다이얼 모드", example = "Predictive")
    @NotBlank String dialMode,
    @Schema(description = "최적화 방식", example = "Availability")
    @NotBlank String optimizeMethod,
    @Schema(description = "최적화 목표", example = "0")
    @Min(0) Integer optimizeGoal
) {
    public int resolvedOptimizeGoal() {
        return optimizeGoal == null ? 0 : optimizeGoal;
    }
}
