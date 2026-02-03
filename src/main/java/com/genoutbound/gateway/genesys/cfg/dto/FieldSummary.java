package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FieldSummary(
        @Schema(description = "Field DBID", example = "21001")
        int dbid,
        @Schema(description = "Field 이름", example = "FIELD_A")
        String name,
        @Schema(description = "Field 타입", example = "CFGFieldTypeStandard")
        String fieldType,
        @Schema(description = "Field 설명", example = "Field 설명")
        String description
) {
}
