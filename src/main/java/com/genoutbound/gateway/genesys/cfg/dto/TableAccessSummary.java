package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TableAccessSummary(
        @Schema(description = "TableAccess DBID", example = "13001")
        int dbid,
        @Schema(description = "TableAccess 이름", example = "TABLE_A")
        String name,
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "설명", example = "TableAccess 설명")
        String description,
        @Schema(description = "TableAccess 타입", example = "CFGTableAccess")
        String type,
        @Schema(description = "DB Access DBID", example = "11001")
        Integer dbAccessDbid,
        @Schema(description = "DB Access 이름", example = "DB_ACCESS_A")
        String dbAccessName,
        @Schema(description = "Format DBID", example = "12001")
        Integer formatDbid,
        @Schema(description = "Format 이름", example = "FORMAT_A")
        String formatName,
        @Schema(description = "DB 테이블명", example = "TABLE_NAME")
        String dbTableName,
        @Schema(description = "캐시 여부", example = "CFGTrue")
        String isCachable,
        @Schema(description = "업데이트 타임아웃", example = "30")
        Integer updateTimeout,
        @Schema(description = "상태", example = "CFGEnabled")
        String state,
        @Schema(description = "사용자 속성")
        java.util.Map<String, Object> userProperties
) {
}
