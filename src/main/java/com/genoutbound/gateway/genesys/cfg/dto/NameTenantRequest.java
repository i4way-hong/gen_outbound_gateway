package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record NameTenantRequest(
    @Schema(description = "이름", example = "NAME_A")
    String name,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}