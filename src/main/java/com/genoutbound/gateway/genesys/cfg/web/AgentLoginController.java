package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginDeleteRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginGetRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginQueryRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginSummary;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.service.AgentLoginConfigService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AgentLogin 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class AgentLoginController {

    private static final Logger log = LoggerFactory.getLogger(AgentLoginController.class);
    private final AgentLoginConfigService agentLoginService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 요청 처리 시 호출만 하며 외부로 노출하지 않습니다.")
    public AgentLoginController(AgentLoginConfigService agentLoginService) {
        this.agentLoginService = agentLoginService;
    }

    @PostMapping("/agent-logins")
    @Operation(summary = "AgentLogin 목록", description = "AgentLogin 목록을 조회합니다.")
    public ApiResponse<List<AgentLoginSummary>> listAgentLogins(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 목록 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = AgentLoginQueryRequest.class),
                examples = @ExampleObject(name = "agentLoginList", value = "{\"tenantDbid\":1,\"switchDbid\":1,\"assignable\":true}")
            )
        )
        @RequestBody(required = false) AgentLoginQueryRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        Integer switchDbid = request == null ? null : request.switchDbid();
        Boolean assignable = request == null ? null : request.assignable();
        log.debug("listAgentLogins 요청: tenantDbid={}, switchDbid={}, assignable={}", tenantDbid, switchDbid, assignable);
        ApiResponse<List<AgentLoginSummary>> response = ApiResponse.ok("AgentLogin 목록",
            agentLoginService.listAgentLogins(tenantDbid, switchDbid, assignable));
        log.debug("listAgentLogins 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/agent-logins/get")
    @Operation(summary = "AgentLogin 조회", description = "AgentLogin을 코드로 조회합니다.")
    public ApiResponse<AgentLoginSummary> getAgentLogin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AgentLoginGetRequest.class),
                examples = @ExampleObject(name = "agentLoginGet", value = "{\"loginCode\":\"1001\",\"tenantDbid\":1,\"switchDbid\":1}")
            )
        )
        @Valid @RequestBody AgentLoginGetRequest request) {
        log.debug("getAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}",
            request.loginCode(), request.tenantDbid(), request.switchDbid());
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 조회",
            agentLoginService.getAgentLoginByCode(request.loginCode(), request.tenantDbid(), request.switchDbid()));
        log.debug("getAgentLogin 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-logins/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "AgentLogin 생성", description = "AgentLogin을 생성합니다.")
    public ApiResponse<AgentLoginSummary> createAgentLogin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AgentLoginRequest.class),
                examples = @ExampleObject(name = "agentLoginCreate", value = "{\"tenantDbid\":1,\"switchDbid\":1,\"code\":\"1001\",\"description\":\"AgentLogin\",\"enabled\":true}")
            )
        )
        @Valid @RequestBody AgentLoginRequest request) {
        log.debug("createAgentLogin 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 생성", agentLoginService.createAgentLogin(request));
        log.debug("createAgentLogin 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-logins/update")
    @Operation(summary = "AgentLogin 수정", description = "AgentLogin 정보를 수정합니다.")
    public ApiResponse<AgentLoginSummary> updateAgentLogin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AgentLoginUpdateCommand.class),
                examples = @ExampleObject(name = "agentLoginUpdate", value = "{\"loginCode\":\"1001\",\"tenantDbid\":1,\"switchDbid\":1,\"payload\":{\"description\":\"AgentLogin\",\"enabled\":true}}")
            )
        )
        @Valid @RequestBody AgentLoginUpdateCommand command) {
        log.debug("updateAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}, payload={}",
            command.loginCode(), command.tenantDbid(), command.switchDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<AgentLoginSummary> response = ApiResponse.ok("AgentLogin 수정",
            agentLoginService.updateAgentLoginByCode(
                command.loginCode(), command.tenantDbid(), command.switchDbid(), command.payload()));
        log.debug("updateAgentLogin 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-logins/delete")
    @Operation(summary = "AgentLogin 삭제", description = "AgentLogin을 삭제합니다.")
    public ApiResponse<Void> deleteAgentLogin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AgentLoginDeleteRequest.class),
                examples = @ExampleObject(name = "agentLoginDelete", value = "{\"loginCode\":\"1001\",\"tenantDbid\":1,\"switchDbid\":1}")
            )
        )
        @Valid @RequestBody AgentLoginDeleteRequest request) {
        log.debug("deleteAgentLogin 요청: loginCode={}, tenantDbid={}, switchDbid={}",
            request.loginCode(), request.tenantDbid(), request.switchDbid());
        agentLoginService.deleteAgentLoginByCode(request.loginCode(), request.tenantDbid(), request.switchDbid());
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 삭제", null);
        log.debug("deleteAgentLogin 응답: {}", response);
        return response;
    }
}
