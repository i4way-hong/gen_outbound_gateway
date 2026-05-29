package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignEmployeeCommand;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignPersonCommand;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupByNameRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupDeleteRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupGetRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupQueryRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.service.AgentGroupConfigService;
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
 * 상담사 그룹(AgentGroup) 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class AgentGroupController {

    // private static final Logger log = LoggerFactory.getLogger(AgentGroupController.class);
    // private final AgentGroupConfigService agentGroupService;

    // @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
    //     justification = "Spring DI 서비스 참조를 요청 처리 시 호출만 하며 외부로 노출하지 않습니다.")
    // public AgentGroupController(AgentGroupConfigService agentGroupService) {
    //     this.agentGroupService = agentGroupService;
    // }

    // @PostMapping("/agent-groups")
    // @Operation(summary = "그룹 목록", description = "상담사 그룹 목록을 조회합니다.")
    // public ApiResponse<List<AgentGroupSummary>> listAgentGroups(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 목록 요청",
    //         required = false,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupQueryRequest.class),
    //             examples = @ExampleObject(name = "agentGroupList", value = "{\"tenantDbid\":101}")
    //         )
    //     )
    //     @RequestBody(required = false) AgentGroupQueryRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listAgentGroups 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<AgentGroupSummary>> response = ApiResponse.ok("그룹 목록", agentGroupService.listAgentGroups(tenantDbid));
    //     log.debug("listAgentGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/agent-groups/get")
    // @Operation(summary = "그룹 조회", description = "상담사 그룹을 DBID로 조회합니다.")
    // public ApiResponse<AgentGroupSummary> getAgentGroup(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 조회 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupGetRequest.class),
    //             examples = @ExampleObject(name = "agentGroupGet", value = "{\"groupDbid\":10,\"tenantDbid\":101}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupGetRequest request) {
    //     log.debug("getAgentGroup 요청: groupDbid={}, tenantDbid={}", request.groupDbid(), request.tenantDbid());
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 조회",
    //         agentGroupService.getAgentGroup(request.groupDbid(), request.tenantDbid()));
    //     log.debug("getAgentGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/by-name")
    // @Operation(summary = "그룹 조회(이름)", description = "상담사 그룹을 이름으로 조회합니다.")
    // public ApiResponse<List<AgentGroupSummary>> getAgentGroupByName(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 조회 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupByNameRequest.class),
    //             examples = @ExampleObject(name = "agentGroupByName", value = "{\"name\":\"Sales\",\"tenantDbid\":101}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupByNameRequest request) {
    //     log.debug("getAgentGroupByName 요청: name={}, tenantDbid={}", request.name(), request.tenantDbid());
    //     ApiResponse<List<AgentGroupSummary>> response = ApiResponse.ok("그룹 조회",
    //         agentGroupService.listAgentGroupsByName(request.name(), request.tenantDbid()));
    //     log.debug("getAgentGroupByName 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/agent-groups/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "그룹 생성", description = "상담사 그룹을 생성합니다.")
    // public ApiResponse<AgentGroupSummary> createAgentGroup(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 생성 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupRequest.class),
    //             examples = @ExampleObject(name = "agentGroupCreate", value = "{\"tenantDbid\":101,\"name\":\"Sales\",\"description\":\"Sales Group\",\"enabled\":true}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupRequest request) {
    // log.debug("createAgentGroup 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 생성", agentGroupService.createAgentGroup(request));
    //     log.debug("createAgentGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/update")
    // @Operation(summary = "그룹 수정", description = "상담사 그룹 정보를 수정합니다.")
    // public ApiResponse<AgentGroupSummary> updateAgentGroup(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 수정 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupUpdateCommand.class),
    //             examples = @ExampleObject(name = "agentGroupUpdate", value = "{\"groupDbid\":10,\"payload\":{\"tenantDbid\":101,\"name\":\"Sales\",\"description\":\"Sales Group\",\"enabled\":true}}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupUpdateCommand command) {
    // log.debug("updateAgentGroup 요청: groupDbid={}, payload={}", command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 수정",
    //         agentGroupService.updateAgentGroup(command.groupDbid(), command.payload()));
    //     log.debug("updateAgentGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/delete")
    // @Operation(summary = "그룹 삭제", description = "상담사 그룹을 삭제합니다.")
    // public ApiResponse<Void> deleteAgentGroup(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 삭제 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupDeleteRequest.class),
    //             examples = @ExampleObject(name = "agentGroupDelete", value = "{\"groupDbid\":10,\"tenantDbid\":101}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupDeleteRequest request) {
    //     log.debug("deleteAgentGroup 요청: groupDbid={}, tenantDbid={}", request.groupDbid(), request.tenantDbid());
    //     agentGroupService.deleteAgentGroup(request.groupDbid(), request.tenantDbid());
    //     ApiResponse<Void> response = ApiResponse.ok("그룹 삭제", null);
    //     log.debug("deleteAgentGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/assign-employee-ids")
    // @Operation(summary = "그룹 배치(사번)", description = "사번 기준으로 그룹 배치를 합니다.")
    // public ApiResponse<AgentGroupSummary> assignAgentGroupByEmployeeIds(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 배치 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupAssignEmployeeCommand.class),
    //             examples = @ExampleObject(name = "assignByEmployee", value = "{\"groupDbid\":10,\"payload\":{\"tenantDbid\":101,\"employeeIds\":[\"E001\",\"E002\"]}}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupAssignEmployeeCommand command) {
    //     log.debug("assignAgentGroupByEmployeeIds 요청: groupDbid={}, payload={}",
    //         command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치",
    //         agentGroupService.assignAgentGroupByEmployeeIds(command.groupDbid(), command.payload()));
    //     log.debug("assignAgentGroupByEmployeeIds 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/assign-person-dbids")
    // @Operation(summary = "그룹 배치(DBID)", description = "상담사 DBID 기준으로 그룹 배치를 합니다.")
    // public ApiResponse<AgentGroupSummary> assignAgentGroupByPersonDbids(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 배치 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupAssignPersonCommand.class),
    //             examples = @ExampleObject(name = "assignByPersonDbid", value = "{\"groupDbid\":10,\"payload\":{\"tenantDbid\":101,\"personDbids\":[2001,2002]}}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupAssignPersonCommand command) {
    //     log.debug("assignAgentGroupByPersonDbids 요청: groupDbid={}, payload={}",
    //         command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치",
    //         agentGroupService.assignAgentGroupByPersonDbids(command.groupDbid(), command.payload()));
    //     log.debug("assignAgentGroupByPersonDbids 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/unassign-employee-ids")
    // @Operation(summary = "그룹 배치 해제(사번)", description = "사번 기준으로 그룹 배치를 해제합니다.")
    // public ApiResponse<AgentGroupSummary> unassignAgentGroupByEmployeeIds(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 배치 해제 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupAssignEmployeeCommand.class),
    //             examples = @ExampleObject(name = "unassignByEmployee", value = "{\"groupDbid\":10,\"payload\":{\"tenantDbid\":101,\"employeeIds\":[\"E001\"]}}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupAssignEmployeeCommand command) {
    //     log.debug("unassignAgentGroupByEmployeeIds 요청: groupDbid={}, payload={}",
    //         command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치 해제",
    //         agentGroupService.unassignAgentGroupByEmployeeIds(command.groupDbid(), command.payload()));
    //     log.debug("unassignAgentGroupByEmployeeIds 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/agent-groups/unassign-person-dbids")
    // @Operation(summary = "그룹 배치 해제(DBID)", description = "상담사 DBID 기준으로 그룹 배치를 해제합니다.")
    // public ApiResponse<AgentGroupSummary> unassignAgentGroupByPersonDbids(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "그룹 배치 해제 요청",
    //         required = true,
    //         content = @Content(
    //             schema = @Schema(implementation = AgentGroupAssignPersonCommand.class),
    //             examples = @ExampleObject(name = "unassignByPersonDbid", value = "{\"groupDbid\":10,\"payload\":{\"tenantDbid\":101,\"personDbids\":[2001]}}")
    //         )
    //     )
    //     @Valid @RequestBody AgentGroupAssignPersonCommand command) {
    //     log.debug("unassignAgentGroupByPersonDbids 요청: groupDbid={}, payload={}",
    //         command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치 해제",
    //         agentGroupService.unassignAgentGroupByPersonDbids(command.groupDbid(), command.payload()));
    //     log.debug("unassignAgentGroupByPersonDbids 응답: {}", response);
    //     return response;
    // }
}
