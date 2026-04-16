package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.PersonAgentLoginCommand;
import com.genoutbound.gateway.genesys.cfg.dto.PersonByEmployeeRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonDeleteRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonGetRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonQueryRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSkillCommand;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PersonUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.service.PersonConfigService;
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
 * 상담사(Person) 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonConfigService personService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 요청 처리 시 호출만 하며 외부로 노출하지 않습니다.")
    public PersonController(PersonConfigService personService) {
        this.personService = personService;
    }

    @PostMapping("/persons")
    @Operation(summary = "상담사 목록", description = "상담사 목록을 조회합니다.")
    public ApiResponse<List<PersonSummary>> listPersons(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 목록 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = PersonQueryRequest.class),
                examples = @ExampleObject(name = "personList", value = "{\"tenantDbid\":101}")
            )
        )
        @RequestBody(required = false) PersonQueryRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listPersons 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<PersonSummary>> response = ApiResponse.ok("상담사 목록", personService.listPersons(tenantDbid));
        log.debug("listPersons 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/persons/get")
    @Operation(summary = "상담사 조회", description = "상담사 DBID로 조회합니다.")
    public ApiResponse<PersonSummary> getPerson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonGetRequest.class),
                examples = @ExampleObject(name = "personGet", value = "{\"personDbid\":10,\"tenantDbid\":101}")
            )
        )
        @Valid @RequestBody PersonGetRequest request) {
        log.debug("getPerson 요청: personDbid={}, tenantDbid={}", request.personDbid(), request.tenantDbid());
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 조회",
            personService.getPerson(request.personDbid(), request.tenantDbid()));
        log.debug("getPerson 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/by-employee")
    @Operation(summary = "상담사 조회(사번)", description = "사번으로 상담사를 조회합니다.")
    public ApiResponse<PersonSummary> getPersonByEmployee(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonByEmployeeRequest.class),
                examples = @ExampleObject(name = "personByEmployee", value = "{\"employeeId\":\"E001\",\"tenantDbid\":101}")
            )
        )
        @Valid @RequestBody PersonByEmployeeRequest request) {
        log.debug("getPersonByEmployee 요청: employeeId={}, tenantDbid={}", request.employeeId(), request.tenantDbid());
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 조회",
            personService.getPersonByEmployeeId(request.employeeId(), request.tenantDbid()));
        log.debug("getPersonByEmployee 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상담사 생성", description = "상담사를 생성합니다.")
    public ApiResponse<PersonSummary> createPerson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonRequest.class),
                examples = @ExampleObject(name = "personCreate", value = "{\"tenantDbid\":101,\"employeeId\":\"E001\",\"firstName\":\"홍\",\"lastName\":\"길동\",\"enabled\":true}")
            )
        )
        @Valid @RequestBody PersonRequest request) {
    log.debug("createPerson 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 생성", personService.createPerson(request));
        log.debug("createPerson 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/update")
    @Operation(summary = "상담사 수정", description = "상담사 정보를 수정합니다.")
    public ApiResponse<PersonSummary> updatePerson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonUpdateCommand.class),
                examples = @ExampleObject(name = "personUpdate", value = "{\"personDbid\":10,\"payload\":{\"tenantDbid\":101,\"firstName\":\"홍\",\"lastName\":\"길동\",\"enabled\":true}}")
            )
        )
        @Valid @RequestBody PersonUpdateCommand command) {
    log.debug("updatePerson 요청: personDbid={}, payload={}", command.personDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<PersonSummary> response = ApiResponse.ok("상담사 수정",
            personService.updatePerson(command.personDbid(), command.payload()));
        log.debug("updatePerson 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/delete")
    @Operation(summary = "상담사 삭제", description = "상담사 정보를 삭제합니다.")
    public ApiResponse<Void> deletePerson(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "상담사 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonDeleteRequest.class),
                examples = @ExampleObject(name = "personDelete", value = "{\"personDbid\":10,\"tenantDbid\":101}")
            )
        )
        @Valid @RequestBody PersonDeleteRequest request) {
        log.debug("deletePerson 요청: personDbid={}", request.personDbid());
        personService.deletePerson(request.personDbid());
        ApiResponse<Void> response = ApiResponse.ok("상담사 삭제", null);
        log.debug("deletePerson 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/skills/set")
    @Operation(summary = "상담사 스킬 설정", description = "상담사 스킬을 설정합니다.")
    public ApiResponse<Void> setPersonSkills(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "스킬 설정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonSkillCommand.class),
                examples = @ExampleObject(name = "personSkillsSet", value = "{\"personDbid\":10,\"payload\":{\"tenantDbid\":101,\"skillDbids\":[1001,1002]}}")
            )
        )
        @Valid @RequestBody PersonSkillCommand command) {
    log.debug("setPersonSkills 요청: personDbid={}, payload={}", command.personDbid(), SensitiveLogMasker.masked(command.payload()));
        personService.setPersonSkills(command.personDbid(), command.payload());
        ApiResponse<Void> response = ApiResponse.ok("상담사 스킬 설정", null);
        log.debug("setPersonSkills 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/skills/delete")
    @Operation(summary = "상담사 스킬 삭제", description = "상담사 스킬을 삭제합니다.")
    public ApiResponse<Void> removePersonSkills(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "스킬 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonSkillCommand.class),
                examples = @ExampleObject(name = "personSkillsDelete", value = "{\"personDbid\":10,\"payload\":{\"tenantDbid\":101,\"skillDbids\":[1001]}}")
            )
        )
        @Valid @RequestBody PersonSkillCommand command) {
    log.debug("removePersonSkills 요청: personDbid={}, payload={}", command.personDbid(), SensitiveLogMasker.masked(command.payload()));
        personService.removePersonSkills(command.personDbid(), command.payload());
        ApiResponse<Void> response = ApiResponse.ok("상담사 스킬 삭제", null);
        log.debug("removePersonSkills 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/agent-logins/by-code/assign")
    @Operation(summary = "AgentLogin 연결(코드)", description = "상담사에 AgentLogin 코드를 연결합니다.")
    public ApiResponse<Void> assignPersonAgentLoginsByCode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 연결 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonAgentLoginCommand.class),
                examples = @ExampleObject(name = "personAgentLoginAssign", value = "{\"personDbid\":10,\"payload\":{\"tenantDbid\":101,\"loginCodes\":[\"1001\",\"1002\"]}}")
            )
        )
        @Valid @RequestBody PersonAgentLoginCommand command) {
    log.debug("assignPersonAgentLoginsByCode 요청: personDbid={}, payload={}", command.personDbid(), SensitiveLogMasker.masked(command.payload()));
        personService.assignPersonAgentLoginsByCode(command.personDbid(), command.payload());
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 연결", null);
        log.debug("assignPersonAgentLoginsByCode 응답: {}", response);
        return response;
    }

    @PostMapping("/persons/agent-logins/by-code/unassign")
    @Operation(summary = "AgentLogin 해제(코드)", description = "상담사에 연결된 AgentLogin 코드를 해제합니다.")
    public ApiResponse<Void> unassignPersonAgentLoginsByCode(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "AgentLogin 해제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = PersonAgentLoginCommand.class),
                examples = @ExampleObject(name = "personAgentLoginUnassign", value = "{\"personDbid\":10,\"payload\":{\"tenantDbid\":101,\"loginCodes\":[\"1001\"]}}")
            )
        )
        @Valid @RequestBody PersonAgentLoginCommand command) {
    log.debug("unassignPersonAgentLoginsByCode 요청: personDbid={}, payload={}", command.personDbid(), SensitiveLogMasker.masked(command.payload()));
        personService.unassignPersonAgentLoginsByCode(command.personDbid(), command.payload());
        ApiResponse<Void> response = ApiResponse.ok("AgentLogin 해제", null);
        log.debug("unassignPersonAgentLoginsByCode 응답: {}", response);
        return response;
    }
}
