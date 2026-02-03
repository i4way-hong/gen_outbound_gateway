package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PlaceRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid,
        @Schema(description = "Place 이름", example = "PLACE_A")
        @NotBlank String name,
        @Schema(description = "DN 번호 목록", example = "[\"1001\",\"1002\"]")
        @NotEmpty List<String> dnNumbers
) {
}
