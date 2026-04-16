package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AgentGroupAssignPersonRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "상담사 DBID 목록", example = "[1001,1002]")
        List<Integer> personDbids
) {

        public AgentGroupAssignPersonRequest {
                personDbids = personDbids == null ? List.of() : List.copyOf(personDbids);
        }

        @Override
        public List<Integer> personDbids() {
                return List.copyOf(personDbids);
        }
}
