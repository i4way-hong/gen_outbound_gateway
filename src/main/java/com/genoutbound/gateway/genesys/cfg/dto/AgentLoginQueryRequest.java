package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentLoginQueryRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "스위치 DBID", example = "101")
    Integer switchDbid,
    @Schema(description = "미할당만 조회", example = "true")
    Boolean assignable
) {
}
