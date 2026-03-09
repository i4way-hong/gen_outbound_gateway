package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionSectionCommand(
    @Schema(description = "트랜잭션 DBID", example = "10001")
    int transactionDbid,
    @Schema(description = "섹션 요청")
    TransactionSectionRequest payload
) {
}