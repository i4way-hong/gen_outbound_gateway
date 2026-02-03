package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PersonAgentLoginCodeRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid,
        @Schema(description = "AgentLogin 코드 목록", example = "[\"1001\",\"1002\"]")
        List<String> loginCodes
) {
}
