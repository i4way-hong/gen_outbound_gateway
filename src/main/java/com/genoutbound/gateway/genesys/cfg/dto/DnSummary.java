package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DnSummary(
        @Schema(description = "DN DBID", example = "9001")
        int dbid,
        @Schema(description = "DN 번호", example = "1001")
        String number,
        @Schema(description = "DN 이름", example = "DN_A")
        String name,
        @Schema(description = "DN 타입", example = "Agent")
        String type,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) {
}
