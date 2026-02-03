package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TransactionRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "트랜잭션 이름", example = "TRX_A")
    @NotBlank String name,
    @Schema(description = "트랜잭션 별칭", example = "TRX_ALIAS")
    String alias,
    @Schema(description = "트랜잭션 타입", example = "Transaction")
    @NotBlank String type,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled
) {
    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
