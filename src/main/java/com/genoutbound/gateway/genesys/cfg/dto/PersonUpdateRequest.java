package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record PersonUpdateRequest(
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
    @Schema(description = "배치할 그룹명 목록", example = "[\"GROUP_A\"]")
    List<String> agentGroupNames
) {
    public boolean isAgent() {
        return agent != null && agent;
    }

    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
