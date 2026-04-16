package com.genoutbound.gateway.genesys.cfg.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@SuppressFBWarnings(value = "NM_CONFUSING",
    justification = "외부 계약 호환성을 위해 person API는 userName 필드명을 유지하고 인증 API는 username을 사용합니다.")
public record PersonRequest(
    @Schema(description = "테넌트 DBID", example = "101")
    Integer tenantDbid,
    @Schema(description = "사번", example = "E1001")
    @NotBlank String employeeId,
    @Schema(description = "사용자 ID", example = "e1001")
    @NotBlank String userName,
    @Schema(description = "이름", example = "길동")
    @NotBlank String firstName,
    @Schema(description = "성", example = "홍")
    @NotBlank String lastName,
    @Schema(description = "AgentLogin ID", example = "1001")
    String agentLoginId,
    @Schema(description = "Agent 여부", example = "true")
    Boolean agent,
    @Schema(description = "활성화 여부", example = "true")
    Boolean enabled,
    @Schema(description = "배치할 그룹명 목록", example = "[\"GROUP_A\",\"GROUP_B\"]")
    List<String> agentGroupNames
) {
    public PersonRequest {
        agentGroupNames = agentGroupNames == null ? List.of() : List.copyOf(agentGroupNames);
    }

    @Override
    public List<String> agentGroupNames() {
        return List.copyOf(agentGroupNames);
    }

    public boolean isAgent() {
        return agent != null && agent;
    }

    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
