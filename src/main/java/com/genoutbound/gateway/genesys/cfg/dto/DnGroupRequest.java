package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DnGroupRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "스위치 DBID", example = "101")
        Integer switchDbid,
        @Schema(description = "DNGroup 이름", example = "DNGROUP_A")
        String name
) {
}
