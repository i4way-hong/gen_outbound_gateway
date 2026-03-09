package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DnTServerOptionCommand(
    @Schema(description = "DN DBID", example = "9001")
    int dnDbid,
    @Schema(description = "TServer 옵션 요청")
    DnTServerOptionRequest payload
) {
}