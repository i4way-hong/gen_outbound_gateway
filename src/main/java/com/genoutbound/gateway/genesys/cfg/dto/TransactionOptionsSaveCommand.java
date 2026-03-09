package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionOptionsSaveCommand(
    @Schema(description = "트랜잭션 DBID", example = "10001")
    int transactionDbid,
    @Schema(description = "옵션 저장 요청")
    TransactionOptionsSaveRequest payload
) {
}