package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public record CampaignRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "캠페인 이름", example = "CMP_A")
    @NotBlank String name,
    @Schema(description = "설명", example = "캠페인 설명")
    String description,
    @Schema(description = "스크립트 DBID", example = "2001")
    Integer scriptDbid,
    @Schema(description = "콜링리스트 이름 목록", example = "[\"LIST_A\",\"LIST_B\"]")
    List<String> callingListNames,
    @Schema(
        description = "사용자 속성 (예: OCServer/CPNDigits)",
        example = "{\"OCServer\":{\"CPNDigits\":\"0311234567\"}}"
    )
    Map<String, Map<String, String>> userProperties,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled
) {
    public CampaignRequest {
        callingListNames = callingListNames == null ? List.of() : List.copyOf(callingListNames);
        userProperties = copyNestedMap(userProperties);
    }

    @Override
    public List<String> callingListNames() {
        return List.copyOf(callingListNames);
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
