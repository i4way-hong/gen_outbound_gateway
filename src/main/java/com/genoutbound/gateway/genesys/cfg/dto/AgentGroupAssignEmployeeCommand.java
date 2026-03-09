package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AgentGroupAssignEmployeeCommand(
    @Schema(description = "그룹 DBID", example = "2001")
    @NotNull Integer groupDbid,
    @Schema(description = "사번 목록")
    @Valid @NotNull AgentGroupAssignEmployeeRequest payload
) {
}
