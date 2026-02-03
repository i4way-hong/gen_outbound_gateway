package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignEmployeeRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignPersonRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupSummary;
import com.genoutbound.gateway.genesys.cfg.service.AgentGroupConfigService;
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
 * 상담사 그룹(AgentGroup) 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class AgentGroupController {

    private static final Logger log = LoggerFactory.getLogger(AgentGroupController.class);
    private final AgentGroupConfigService agentGroupService;

    public AgentGroupController(AgentGroupConfigService agentGroupService) {
        this.agentGroupService = agentGroupService;
    }

    @GetMapping("/agent-groups")
    @Operation(summary = "그룹 목록", description = "상담사 그룹 목록을 조회합니다.")
    public ApiResponse<List<AgentGroupSummary>> listAgentGroups(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listAgentGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<AgentGroupSummary>> response = ApiResponse.ok("그룹 목록", agentGroupService.listAgentGroups(tenantDbid));
        log.debug("listAgentGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/agent-groups/{groupDbid}")
    @Operation(summary = "그룹 조회", description = "상담사 그룹을 DBID로 조회합니다.")
    public ApiResponse<AgentGroupSummary> getAgentGroup(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getAgentGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 조회", agentGroupService.getAgentGroup(groupDbid, tenantDbid));
        log.debug("getAgentGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/agent-groups/by-name")
    @Operation(summary = "그룹 조회(이름)", description = "상담사 그룹을 이름으로 조회합니다.")
    public ApiResponse<List<AgentGroupSummary>> getAgentGroupByName(
        @Parameter(description = "그룹 이름", example = "AGENT_GROUP_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getAgentGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<List<AgentGroupSummary>> response = ApiResponse.ok("그룹 조회",
            agentGroupService.listAgentGroupsByName(name, tenantDbid));
        log.debug("getAgentGroupByName 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/agent-groups")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "그룹 생성", description = "상담사 그룹을 생성합니다.")
    public ApiResponse<AgentGroupSummary> createAgentGroup(
        @Parameter(description = "그룹 생성 요청")
        @Valid @RequestBody AgentGroupRequest request) {
        log.debug("createAgentGroup 요청: {}", request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 생성", agentGroupService.createAgentGroup(request));
        log.debug("createAgentGroup 응답: {}", response);
        return response;
    }

    @PutMapping("/agent-groups/{groupDbid}")
    @Operation(summary = "그룹 수정", description = "상담사 그룹 정보를 수정합니다.")
    public ApiResponse<AgentGroupSummary> updateAgentGroup(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "그룹 수정 요청")
        @Valid @RequestBody AgentGroupRequest request) {
        log.debug("updateAgentGroup 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 수정", agentGroupService.updateAgentGroup(groupDbid, request));
        log.debug("updateAgentGroup 응답: {}", response);
        return response;
    }

    @DeleteMapping("/agent-groups/{groupDbid}")
    @Operation(summary = "그룹 삭제", description = "상담사 그룹을 삭제합니다.")
    public ApiResponse<Void> deleteAgentGroup(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteAgentGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        agentGroupService.deleteAgentGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("그룹 삭제", null);
        log.debug("deleteAgentGroup 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-groups/{groupDbid}/assign-employee-ids")
    @Operation(summary = "그룹 배치(사번)", description = "사번 기준으로 그룹 배치를 합니다.")
    public ApiResponse<AgentGroupSummary> assignAgentGroupByEmployeeIds(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "사번 목록")
        @Valid @RequestBody AgentGroupAssignEmployeeRequest request) {
        log.debug("assignAgentGroupByEmployeeIds 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치",
            agentGroupService.assignAgentGroupByEmployeeIds(groupDbid, request));
        log.debug("assignAgentGroupByEmployeeIds 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-groups/{groupDbid}/assign-person-dbids")
    @Operation(summary = "그룹 배치(DBID)", description = "상담사 DBID 기준으로 그룹 배치를 합니다.")
    public ApiResponse<AgentGroupSummary> assignAgentGroupByPersonDbids(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "상담사 DBID 목록")
        @Valid @RequestBody AgentGroupAssignPersonRequest request) {
        log.debug("assignAgentGroupByPersonDbids 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치",
            agentGroupService.assignAgentGroupByPersonDbids(groupDbid, request));
        log.debug("assignAgentGroupByPersonDbids 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-groups/{groupDbid}/unassign-employee-ids")
    @Operation(summary = "그룹 배치 해제(사번)", description = "사번 기준으로 그룹 배치를 해제합니다.")
    public ApiResponse<AgentGroupSummary> unassignAgentGroupByEmployeeIds(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "사번 목록")
        @Valid @RequestBody AgentGroupAssignEmployeeRequest request) {
        log.debug("unassignAgentGroupByEmployeeIds 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치 해제",
            agentGroupService.unassignAgentGroupByEmployeeIds(groupDbid, request));
        log.debug("unassignAgentGroupByEmployeeIds 응답: {}", response);
        return response;
    }

    @PostMapping("/agent-groups/{groupDbid}/unassign-person-dbids")
    @Operation(summary = "그룹 배치 해제(DBID)", description = "상담사 DBID 기준으로 그룹 배치를 해제합니다.")
    public ApiResponse<AgentGroupSummary> unassignAgentGroupByPersonDbids(
        @Parameter(description = "그룹 DBID", example = "2001")
        @PathVariable int groupDbid,
        @Parameter(description = "상담사 DBID 목록")
        @Valid @RequestBody AgentGroupAssignPersonRequest request) {
        log.debug("unassignAgentGroupByPersonDbids 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<AgentGroupSummary> response = ApiResponse.ok("그룹 배치 해제",
            agentGroupService.unassignAgentGroupByPersonDbids(groupDbid, request));
        log.debug("unassignAgentGroupByPersonDbids 응답: {}", response);
        return response;
    }
}
