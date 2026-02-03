package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionSummary(
        @Schema(description = "트랜잭션 DBID", example = "10001")
        int dbid,
        @Schema(description = "트랜잭션 이름", example = "TRX_A")
        String name,
        @Schema(description = "트랜잭션 별칭", example = "TRX_ALIAS")
        String alias,
        @Schema(description = "트랜잭션 타입", example = "Transaction")
        String type,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) {
}
