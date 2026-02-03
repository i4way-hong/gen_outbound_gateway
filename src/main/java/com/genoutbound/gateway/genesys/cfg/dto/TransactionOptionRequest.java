package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TransactionOptionRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "섹션 이름", example = "SECTION_A")
        @NotBlank String sectionName,
        @Schema(description = "옵션 키", example = "KEY_A")
        @NotBlank String key,
        @Schema(description = "변경할 옵션 키", example = "KEY_B")
        String changeKey,
        @Schema(description = "옵션 값", example = "VALUE")
        String value,
        @Schema(description = "데이터 타입", example = "String")
        String dataType,
        @Schema(description = "별칭", example = "ALIAS")
        String alias
) {
}
