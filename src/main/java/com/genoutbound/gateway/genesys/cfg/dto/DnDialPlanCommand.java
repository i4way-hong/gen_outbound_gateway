package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

public record DnDialPlanCommand(
    @Schema(description = "DN DBID", example = "9001")
    int dnDbid,
    @Schema(description = "DialPlan 설정 요청")
    DnDialPlanRequest payload
) implements Serializable {
}