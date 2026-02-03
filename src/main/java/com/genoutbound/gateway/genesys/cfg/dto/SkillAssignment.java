package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SkillAssignment(
        @Schema(description = "스킬 DBID", example = "5001")
        int skillDbid,
        @Schema(description = "스킬 레벨", example = "3")
        int level
) {
}
