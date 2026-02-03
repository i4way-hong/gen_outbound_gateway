package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record TransactionOptionsSaveRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "섹션 이름", example = "SECTION_A")
        @NotBlank String sectionName,
        @Schema(description = "옵션 목록", example = "{\"KEY\":\"VALUE\"}")
        Map<String, String> options
) {
}
