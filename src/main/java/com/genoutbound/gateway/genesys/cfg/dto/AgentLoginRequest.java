package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentLoginRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid,
        @Schema(description = "로그인 코드", example = "1001")
        String loginCode,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) {
}
