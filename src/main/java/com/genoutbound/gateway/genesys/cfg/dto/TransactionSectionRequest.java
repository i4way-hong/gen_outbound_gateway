package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TransactionSectionRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "섹션 이름", example = "SECTION_A")
        @NotBlank String sectionName,
        @Schema(description = "변경할 섹션 이름", example = "SECTION_B")
        String changeSectionName
) {
}
