package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceGroupSummary(
        @Schema(description = "PlaceGroup DBID", example = "11001")
        int dbid,
        @Schema(description = "PlaceGroup 이름", example = "PLACE_GROUP_A")
        String name
) {
}
