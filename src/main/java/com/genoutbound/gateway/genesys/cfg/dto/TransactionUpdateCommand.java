package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionUpdateCommand(
    @Schema(description = "트랜잭션 DBID", example = "10001")
    int transactionDbid,
    @Schema(description = "트랜잭션 수정 요청")
    TransactionRequest payload
) {
}