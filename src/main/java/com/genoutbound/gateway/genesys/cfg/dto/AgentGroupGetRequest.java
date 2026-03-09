package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AgentGroupGetRequest(
    @Schema(description = "그룹 DBID", example = "2001")
    @NotNull Integer groupDbid,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}
