package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AgentGroupSummary(
        @Schema(description = "그룹 DBID", example = "2001")
        int dbid,
        @Schema(description = "그룹명", example = "GROUP_A")
        String name,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled,
        @Schema(description = "그룹에 속한 상담사 목록")
        List<AgentGroupMemberSummary> members
) {
}
