package com.genoutbound.gateway.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ScsAppStatusRequest(
    @Schema(description = "Application DBID", example = "107")
    Integer dbid
) {
}