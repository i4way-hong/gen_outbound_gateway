package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentLoginUpdateRequest(
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) {
}