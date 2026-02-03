package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceGroupRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "PlaceGroup 이름", example = "PLACE_GROUP_A")
        String name
) {
}
