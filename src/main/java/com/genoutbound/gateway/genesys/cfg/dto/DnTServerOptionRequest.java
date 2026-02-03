package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public record DnTServerOptionRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "TServer 옵션", example = "{\"key\":\"value\"}")
        Map<String, String> options
) {
}
