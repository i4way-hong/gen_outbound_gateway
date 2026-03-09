package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PersonGetRequest(
    @Schema(description = "상담사 DBID", example = "1001")
    @NotNull Integer personDbid,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}
