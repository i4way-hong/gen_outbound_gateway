package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AgentGroupByNameRequest(
    @Schema(description = "그룹 이름", example = "AGENT_GROUP_A")
    @NotBlank String name,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}
