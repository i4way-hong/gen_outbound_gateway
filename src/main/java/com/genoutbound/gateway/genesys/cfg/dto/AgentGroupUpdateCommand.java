package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AgentGroupUpdateCommand(
    @Schema(description = "그룹 DBID", example = "2001")
    @NotNull Integer groupDbid,
    @Schema(description = "그룹 수정 요청")
    @Valid @NotNull AgentGroupRequest payload
) {
}
