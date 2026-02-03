package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginSummary;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginUpdateRequest;
import com.genoutbound.gateway.genesys.cfg.service.AgentLoginConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AgentLogin 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class AgentLoginController {

    private static final Logger log = LoggerFactory.getLogger(AgentLoginController.class);
    private final AgentLoginConfigService agentLoginService;

    public AgentLoginController(AgentLoginConfigService agentLoginService) {
        this.agentLoginService = agentLoginService;
    }

    @GetMapping("/agent-logins")
    @Operation(summary = "AgentLogin 목록", description = "AgentLogin 목록을 조회합니다.")
    public ApiResponse<List<AgentLoginSummary>> listAgentLogins(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid,
        @Parameter(description = "스위치 DBID", example = "101")
        @RequestParam(required = false) Integer switchDbid,
        @Parameter(description = "미할당만 조회", example = "true")
        @RequestParam(required = false) Boolean assignable) {
        log.debug("listAgentLogins 요청: tenantDbid={}, switchDbid={}, assignable={}", tenantDbid, switchDbid, assignable);
        ApiResponse<List<AgentLoginSummary>> response = ApiResponse.ok("AgentLogin 목록",
            agentLoginService.listAgentLogins(tenantDbid, switchDbid, assignable));
        log.debug("listAgentLogins 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/agent-logins/{loginCode}")
    @Operation(summary = "AgentLogin 조회", description = "AgentLogin을 코드로 조회합니다.")
    public ApiResponse<AgentLoginSummary> getAgentLogin(
        @Parameter(description = "AgentLogin 코드", example = "1001")
        @PathVariable String loginCode,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid,
        @Parameter(description = "스위치 DBID", example = "101")
        @RequestParam(required = false) Integer switchDbid) {
        log.debug("getAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}", loginCode, tenantDbid, switchDbid);
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 조회",
            agentLoginService.getAgentLoginByCode(loginCode, tenantDbid, switchDbid));
        log.debug("getAgentLogin 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-logins")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "AgentLogin 생성", description = "AgentLogin을 생성합니다.")
    public ApiResponse<AgentLoginSummary> createAgentLogin(
        @Parameter(description = "AgentLogin 생성 요청")
        @Valid @RequestBody AgentLoginRequest request) {
        log.debug("createAgentLogin 요청: {}", request);
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 생성", agentLoginService.createAgentLogin(request));
        log.debug("createAgentLogin 응답: {}", response);
        return response;
    }

    @PutMapping("/agent-logins/{loginCode}")
    @Operation(summary = "AgentLogin 수정", description = "AgentLogin 정보를 수정합니다.")
    public ApiResponse<AgentLoginSummary> updateAgentLogin(
        @Parameter(description = "AgentLogin 코드", example = "1001")
        @PathVariable String loginCode,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid,
        @Parameter(description = "스위치 DBID", example = "101")
        @RequestParam(required = false) Integer switchDbid,
        @Parameter(description = "AgentLogin 수정 요청")
        @Valid @RequestBody AgentLoginUpdateRequest request) {
        log.debug("updateAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}, request={}",
            loginCode, tenantDbid, switchDbid, request);
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 수정",
            agentLoginService.updateAgentLoginByCode(loginCode, tenantDbid, switchDbid, request));
        log.debug("updateAgentLogin 응답: {}", response);
        return response;
    }

    @DeleteMapping("/agent-logins/{loginCode}")
    @Operation(summary = "AgentLogin 삭제", description = "AgentLogin을 삭제합니다.")
    public ApiResponse<Void> deleteAgentLogin(
        @Parameter(description = "AgentLogin 코드", example = "1001")
        @PathVariable String loginCode,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid,
        @Parameter(description = "스위치 DBID", example = "101")
        @RequestParam(required = false) Integer switchDbid) {
        log.debug("deleteAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}", loginCode, tenantDbid, switchDbid);
        agentLoginService.deleteAgentLoginByCode(loginCode, tenantDbid, switchDbid);
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 삭제", null);
        log.debug("deleteAgentLogin 응답: {}", response);
        return response;
    }
}
