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
    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
