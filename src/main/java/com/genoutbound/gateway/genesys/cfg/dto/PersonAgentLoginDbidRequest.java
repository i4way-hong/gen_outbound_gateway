package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Deprecated
public record PersonAgentLoginDbidRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid,
        @Schema(description = "AgentLogin 코드 목록(Deprecated: DBID 대신 코드 사용)", example = "[\"1001\",\"1002\"]")
        List<String> loginCodes
) {
}
