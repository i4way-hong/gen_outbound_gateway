package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.PersonAgentLoginCodeRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSkillRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PersonUpdateRequest;
import com.genoutbound.gateway.genesys.cfg.service.PersonConfigService;
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
 * 상담사(Person) 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonConfigService personService;

    public PersonController(PersonConfigService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    @Operation(summary = "상담사 목록", description = "상담사 목록을 조회합니다.")
    public ApiResponse<List<PersonSummary>> listPersons(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listPersons 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<PersonSummary>> response = ApiResponse.ok("상담사 목록", personService.listPersons(tenantDbid));
        log.debug("listPersons 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/persons/{personDbid}")
    @Operation(summary = "상담사 조회", description = "상담사 DBID로 조회합니다.")
    public ApiResponse<PersonSummary> getPerson(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPerson 요청: personDbid={}, tenantDbid={}", personDbid, tenantDbid);
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 조회", personService.getPerson(personDbid, tenantDbid));
        log.debug("getPerson 응답: {}", response);
        return response;
    }

    @GetMapping("/persons/by-employee")
    @Operation(summary = "상담사 조회(사번)", description = "사번으로 상담사를 조회합니다.")
    public ApiResponse<PersonSummary> getPersonByEmployee(
        @Parameter(description = "사번", example = "E1001")
        @RequestParam String employeeId,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPersonByEmployee 요청: employeeId={}, tenantDbid={}", employeeId, tenantDbid);
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 조회",
            personService.getPersonByEmployeeId(employeeId, tenantDbid));
        log.debug("getPersonByEmployee 응답: {}", response);
        return response;
    }

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상담사 생성", description = "상담사를 생성합니다.")
    public ApiResponse<PersonSummary> createPerson(
        @Parameter(description = "상담사 생성 요청")
        @Valid @RequestBody PersonRequest request) {
        log.debug("createPerson 요청: {}", request);
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 생성", personService.createPerson(request));
        log.debug("createPerson 응답: {}", response);
        return response;
    }

    @PutMapping("/persons/{personDbid}")
    @Operation(summary = "상담사 수정", description = "상담사 정보를 수정합니다.")
    public ApiResponse<PersonSummary> updatePerson(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "상담사 수정 요청")
        @Valid @RequestBody PersonUpdateRequest request) {
        log.debug("updatePerson 요청: personDbid={}, request={}", personDbid, request);
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 수정", personService.updatePerson(personDbid, request));
        log.debug("updatePerson 응답: {}", response);
        return response;
    }

    @DeleteMapping("/persons/{personDbid}")
    @Operation(summary = "상담사 삭제", description = "상담사 정보를 삭제합니다.")
    public ApiResponse<Void> deletePerson(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid) {
        log.debug("deletePerson 요청: personDbid={}", personDbid);
        personService.deletePerson(personDbid);
        ApiResponse<Void> response = ApiResponse.ok("상담사 삭제", null);
        log.debug("deletePerson 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/{personDbid}/skills")
    @Operation(summary = "상담사 스킬 설정", description = "상담사 스킬을 설정합니다.")
    public ApiResponse<Void> setPersonSkills(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "스킬 설정 요청")
        @Valid @RequestBody PersonSkillRequest request) {
        log.debug("setPersonSkills 요청: personDbid={}, request={}", personDbid, request);
        personService.setPersonSkills(personDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("상담사 스킬 설정", null);
        log.debug("setPersonSkills 응답: {}", response);
        return response;
    }

    @DeleteMapping("/persons/{personDbid}/skills")
    @Operation(summary = "상담사 스킬 삭제", description = "상담사 스킬을 삭제합니다.")
    public ApiResponse<Void> removePersonSkills(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "스킬 삭제 요청")
        @RequestBody PersonSkillRequest request) {
        log.debug("removePersonSkills 요청: personDbid={}, request={}", personDbid, request);
        personService.removePersonSkills(personDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("상담사 스킬 삭제", null);
        log.debug("removePersonSkills 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/{personDbid}/agent-logins/by-code")
    @Operation(summary = "AgentLogin 연결(코드)", description = "상담사에 AgentLogin 코드를 연결합니다.")
    public ApiResponse<Void> assignPersonAgentLoginsByCode(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "AgentLogin 코드 목록")
        @Valid @RequestBody PersonAgentLoginCodeRequest request) {
        log.debug("assignPersonAgentLoginsByCode 요청: personDbid={}, request={}", personDbid, request);
        personService.assignPersonAgentLoginsByCode(personDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 연결", null);
        log.debug("assignPersonAgentLoginsByCode 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/{personDbid}/agent-logins/by-code/unassign")
    @Operation(summary = "AgentLogin 해제(코드)", description = "상담사에 연결된 AgentLogin 코드를 해제합니다.")
    public ApiResponse<Void> unassignPersonAgentLoginsByCode(
        @Parameter(description = "상담사 DBID", example = "1001")
        @PathVariable int personDbid,
        @Parameter(description = "AgentLogin 코드 목록")
        @Valid @RequestBody PersonAgentLoginCodeRequest request) {
        log.debug("unassignPersonAgentLoginsByCode 요청: personDbid={}, request={}", personDbid, request);
        personService.unassignPersonAgentLoginsByCode(personDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 해제", null);
        log.debug("unassignPersonAgentLoginsByCode 응답: {}", response);
        return response;
    }
}
