package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public record CallingListDetailRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "콜링리스트 이름", example = "LIST_A")
    @NotBlank String name,
    @Schema(description = "설명", example = "콜링리스트 설명")
    String description,
    @Schema(description = "Filter DBID", example = "4001")
    Integer filterDbid,
    @Schema(description = "로그 테이블 액세스 DBID", example = "3001")
    Integer logTableAccessDbid,
    @Schema(description = "최대 시도 횟수", example = "3")
    Integer maxAttempts,
    @Schema(description = "스크립트 DBID", example = "2001")
    Integer scriptDbid,
    @Schema(description = "테이블 액세스 DBID", example = "1001")
    Integer tableAccessDbid,
    @Schema(description = "허용 시작 시간", example = "800")
    Integer timeFrom,
    @Schema(description = "허용 종료 시간", example = "1800")
    Integer timeTo,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled,
    @Schema(description = "트리트먼트 DBID 목록", example = "[100,200]")
    List<Integer> treatmentDbids,
    @Schema(
        description = "사용자 속성 (예: OCServer/CPNDigits)",
        example = "{\"OCServer\":{\"CPNDigits\":\"0311234567\"}}"
    )
    Map<String, Map<String, String>> userProperties
) {
    public int resolvedMaxAttempts() {
        return maxAttempts == null ? 1 : maxAttempts;
    }

    public boolean resolvedEnabled() {
        return enabled == null || enabled;
    }
}
