package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PersonByEmployeeRequest(
    @Schema(description = "사번", example = "E1001")
    @NotBlank String employeeId,
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid
) {
}
