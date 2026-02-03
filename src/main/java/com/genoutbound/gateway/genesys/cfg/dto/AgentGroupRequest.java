package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AgentGroupRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "그룹명", example = "GROUP_A")
    @NotBlank String name,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled
) {
    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
