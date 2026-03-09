package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OutboundBatchCreateCommand(
    @Schema(description = "아웃바운드 배치 생성 요청")
    OutboundBatchCreateRequest request,
    @Schema(description = "상세 응답 여부", example = "true")
    Boolean detail
) {
}