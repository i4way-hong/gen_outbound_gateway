package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record TreatmentUpdateCommand(
    @Schema(description = "Treatment DBID", example = "14001")
    @NotNull Integer treatmentDbid,
    @Schema(description = "Treatment 수정 요청")
    @NotNull @Valid TreatmentRequest payload
) {
}
