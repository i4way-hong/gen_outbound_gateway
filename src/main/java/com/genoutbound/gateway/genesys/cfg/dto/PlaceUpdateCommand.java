package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceUpdateCommand(
    @Schema(description = "Place DBID", example = "12001")
    int placeDbid,
    @Schema(description = "Place 수정 요청")
    PlaceRequest payload
) {
}