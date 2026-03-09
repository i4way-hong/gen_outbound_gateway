package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionOptionCommand(
    @Schema(description = "트랜잭션 DBID", example = "10001")
    int transactionDbid,
    @Schema(description = "옵션 요청")
    TransactionOptionRequest payload
) {
}