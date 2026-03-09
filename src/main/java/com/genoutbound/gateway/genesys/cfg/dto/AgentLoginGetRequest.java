package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AgentLoginGetRequest(
    @Schema(description = "AgentLogin 코드", example = "1001")
    @NotBlank String loginCode,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "스위치 DBID", example = "101")
    Integer switchDbid
) {
}
