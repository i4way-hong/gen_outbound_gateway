package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentGroupMemberSummary(
    @Schema(description = "상담사 DBID", example = "1001")
    int dbid,
    @Schema(description = "이름", example = "길동")
    String firstName,
    @Schema(description = "성", example = "홍")
    String lastName,
    @Schema(description = "사번", example = "E1001")
    String employeeId
) {
}
