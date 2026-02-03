package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DnRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "스위치 DBID", example = "101")
    Integer switchDbid,
    @Schema(description = "DN 번호", example = "1001")
    @NotBlank String number,
    @Schema(description = "DN 이름", example = "DN_A")
    String name,
    @Schema(description = "DN 타입", example = "Agent")
    @NotBlank String type,
    @Schema(description = "등록 플래그", example = "CFGRegisterAll")
    @NotBlank String registerFlag,
    @Schema(description = "라우트 타입", example = "Default")
    @NotBlank String routeType,
    @Schema(description = "스위치 고유 타입", example = "0")
    Integer switchSpecificType,
    @Schema(description = "트렁크 수", example = "0")
    Integer trunks,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled
) {
    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
