package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FilterUpdateCommand(
    @Schema(description = "Filter DBID", example = "4001")
    int filterDbid,
    @Schema(description = "Filter 수정 요청")
    FilterRequest payload
) {
}