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
        public FormatSummary {
                fields = fields == null ? java.util.List.of() : java.util.List.copyOf(fields);
                userProperties = copyNestedMap(userProperties);
        }

        @Override
        public java.util.List<FieldSummary> fields() {
                return java.util.List.copyOf(fields);
        }

        @Override
        public java.util.Map<String, java.util.Map<String, String>> userProperties() {
                return copyNestedMap(userProperties);
        }

        private static java.util.Map<String, java.util.Map<String, String>> copyNestedMap(
                        java.util.Map<String, java.util.Map<String, String>> source
        ) {
                if (source == null || source.isEmpty()) {
                        return java.util.Map.of();
                }
                java.util.Map<String, java.util.Map<String, String>> copied = new java.util.LinkedHashMap<>();
                for (java.util.Map.Entry<String, java.util.Map<String, String>> entry : source.entrySet()) {
                        java.util.Map<String, String> value = entry.getValue();
                        copied.put(entry.getKey(), value == null ? java.util.Map.of() : java.util.Map.copyOf(value));
                }
                return java.util.Map.copyOf(copied);
        }
}
