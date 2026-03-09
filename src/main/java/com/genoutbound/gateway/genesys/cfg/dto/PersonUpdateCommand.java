package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PersonUpdateCommand(
    @Schema(description = "상담사 DBID", example = "1001")
    @NotNull Integer personDbid,
    @Schema(description = "상담사 수정 요청")
    @Valid @NotNull PersonUpdateRequest payload
) {
}
