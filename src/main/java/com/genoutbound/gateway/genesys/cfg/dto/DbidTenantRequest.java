package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DbidTenantRequest(
    @Schema(description = "대상 DBID", example = "10001")
    Integer dbid,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}