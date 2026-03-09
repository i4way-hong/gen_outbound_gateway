package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DnUpdateCommand(
    @Schema(description = "DN DBID", example = "9001")
    int dnDbid,
    @Schema(description = "DN 수정 요청")
    DnRequest payload
) {
}