package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DnGroupSummary(
        @Schema(description = "DNGroup DBID", example = "8001")
        int dbid,
        @Schema(description = "DNGroup 이름", example = "DNGROUP_A")
        String name,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid
) {
}
