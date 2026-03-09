package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AgentGroupAssignPersonCommand(
    @Schema(description = "그룹 DBID", example = "2001")
    @NotNull Integer groupDbid,
    @Schema(description = "상담사 DBID 목록")
    @Valid @NotNull AgentGroupAssignPersonRequest payload
) {
}
