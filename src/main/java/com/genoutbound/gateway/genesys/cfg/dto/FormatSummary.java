package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FormatSummary(
        @Schema(description = "Format DBID", example = "12001")
        int dbid,
        @Schema(description = "Format 이름", example = "FORMAT_A")
        String name,
        @Schema(description = "설명", example = "Format 설명")
        String description,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled,
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "Field 목록")
        java.util.List<FieldSummary> fields,
        @Schema(description = "사용자 속성")
        java.util.Map<String, java.util.Map<String, String>> userProperties
) {
}
