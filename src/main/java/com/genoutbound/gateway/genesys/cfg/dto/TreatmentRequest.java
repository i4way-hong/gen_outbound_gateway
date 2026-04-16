package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record TreatmentRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "Treatment 이름", example = "TREATMENT_A")
    @NotBlank String name,
    @Schema(description = "설명", example = "Treatment 설명")
    String description,
    @Schema(description = "콜 결과", example = "Answer")
    String callResult,
    @Schema(description = "녹취 액션 코드", example = "CFGRACRetryIn")
    @NotBlank String recActionCode,
    @Schema(description = "시도 횟수", example = "3")
    Integer attempts,
    @Schema(description = "재시도 시각(ISO-8601)", example = "2026-03-20T10:30:00+09:00")
    String dateTime,
    @Schema(description = "순환 시도 횟수", example = "5")
    Integer cycleAttempt,
    @Schema(description = "간격(분)", example = "10")
    Integer interval,
    @Schema(description = "증분(분)", example = "5")
    Integer increment,
    @Schema(description = "콜 액션 코드", example = "CFGCACTreatment")
    String callActionCode,
    @Schema(description = "대상 DN DBID", example = "15001")
    Integer destDnDbid,
    @Schema(description = "사용자 속성", example = "{\"default\":{\"key\":\"value\"}}")
    Map<String, Map<String, String>> userProperties,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled
) {
    public TreatmentRequest {
        userProperties = copyNestedMap(userProperties);
    }

    @Override
    public Map<String, Map<String, String>> userProperties() {
        return copyNestedMap(userProperties);
    }

    private static Map<String, Map<String, String>> copyNestedMap(Map<String, Map<String, String>> source) {
        if (source == null || source.isEmpty()) {
            return Map.of();
        }
        Map<String, Map<String, String>> copied = new java.util.LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : source.entrySet()) {
            Map<String, String> value = entry.getValue();
            copied.put(entry.getKey(), value == null ? Map.of() : Map.copyOf(value));
        }
        return Map.copyOf(copied);
    }

    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
