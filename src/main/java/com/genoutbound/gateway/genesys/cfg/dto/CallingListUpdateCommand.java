package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CallingListUpdateCommand(
    @Schema(description = "콜링리스트 DBID", example = "5001")
    int callingListDbid,
    @Schema(description = "콜링리스트 수정 요청")
    CallingListDetailRequest payload
) {
}