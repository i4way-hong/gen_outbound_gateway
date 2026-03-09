package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgentLoginUpdateCommand(
    @Schema(description = "AgentLogin 코드", example = "1001")
    @NotBlank String loginCode,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "스위치 DBID", example = "101")
    Integer switchDbid,
    @Schema(description = "AgentLogin 수정 요청")
    @Valid @NotNull AgentLoginUpdateRequest payload
) {
}
