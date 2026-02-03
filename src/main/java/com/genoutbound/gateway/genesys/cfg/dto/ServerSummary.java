package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ServerSummary(
        @Schema(description = "서버 DBID", example = "3001")
        Integer dbid,
        @Schema(description = "서버 이름", example = "OCS_A")
        String name
) {
}
