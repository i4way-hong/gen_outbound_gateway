package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DnDialPlanRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "DialPlan 활성화 여부", example = "true")
        @NotNull Boolean enabled,
        @Schema(description = "DialPlan 이름", example = "DIALPLAN_A")
        String dialPlanName
) {
}
