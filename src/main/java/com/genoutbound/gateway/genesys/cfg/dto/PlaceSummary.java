package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceSummary(
        @Schema(description = "Place DBID", example = "12001")
        int dbid,
        @Schema(description = "Place 이름", example = "PLACE_A")
        String name,
        @Schema(description = "DN 수", example = "2")
        int dnCount
) {
}
