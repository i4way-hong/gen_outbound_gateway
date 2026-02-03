package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AgentGroupAssignEmployeeRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "사번 목록", example = "[\"E1001\",\"E1002\"]")
        List<String> employeeIds
) {
}
