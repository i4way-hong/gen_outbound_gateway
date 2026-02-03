package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentLoginSummary(
        @Schema(description = "AgentLogin DBID", example = "3001")
        int dbid,
        @Schema(description = "로그인 코드", example = "1001")
        String loginCode,
        @Schema(description = "스위치 DBID", example = "101")
        int switchDbid,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled,
        @Schema(description = "할당 가능 여부", example = "true")
        boolean assignable
) {
}
