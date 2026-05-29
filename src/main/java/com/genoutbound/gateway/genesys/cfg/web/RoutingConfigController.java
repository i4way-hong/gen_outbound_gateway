package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnDialPlanCommand;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnTServerOptionCommand;
import com.genoutbound.gateway.genesys.cfg.dto.DnUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionCommand;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionsSaveCommand;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSectionCommand;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.service.RoutingConfigService;
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
 * DN/Transaction/Place 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class RoutingConfigController {

    // private static final Logger log = LoggerFactory.getLogger(RoutingConfigController.class);
    // private final RoutingConfigService routingService;

    // @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
    //     justification = "Spring DI 서비스 참조를 요청 처리 시 호출만 하며 외부로 노출하지 않습니다.")
    // public RoutingConfigController(RoutingConfigService routingService) {
    //     this.routingService = routingService;
    // }

    // @PostMapping("/dns")
    // @Operation(summary = "DN 목록", description = "DN 목록을 조회합니다.")
    // public ApiResponse<List<DnSummary>> listDns(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 조회 요청",
    //             required = false,
    //             content = @Content(
    //                 schema = @Schema(implementation = TenantDbidRequest.class),
    //                 examples = @ExampleObject(name = "dnList", value = "{\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody TenantDbidRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listDns 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<DnSummary>> response = ApiResponse.ok("DN 목록", routingService.listDns(tenantDbid));
    //     log.debug("listDns 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/dn-groups")
    // @Operation(summary = "DNGroup 목록", description = "DNGroup 목록을 조회합니다.")
    // public ApiResponse<List<DnGroupSummary>> listDnGroups(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DNGroup 조회 요청",
    //             required = false,
    //             content = @Content(
    //                 schema = @Schema(implementation = TenantDbidRequest.class),
    //                 examples = @ExampleObject(name = "dnGroupList", value = "{\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody TenantDbidRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listDnGroups 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<DnGroupSummary>> response = ApiResponse.ok("DNGroup 목록", routingService.listDnGroups(tenantDbid));
    //     log.debug("listDnGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/dn-groups/get")
    // @Operation(summary = "DNGroup 조회", description = "DNGroup을 DBID로 조회합니다.")
    // public ApiResponse<DnGroupSummary> getDnGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DNGroup 조회 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnGroupGet", value = "{\"dbid\":8001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int groupDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
    //     ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 조회", routingService.getDnGroup(groupDbid, tenantDbid));
    //     log.debug("getDnGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dn-groups/by-name")
    // @Operation(summary = "DNGroup 조회(이름)", description = "DNGroup을 이름으로 조회합니다.")
    // public ApiResponse<DnGroupSummary> getDnGroupByName(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DNGroup 조회(이름) 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = NameTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnGroupByName", value = "{\"name\":\"DNGROUP_A\",\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody NameTenantRequest request) {
    //     String name = request.name();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getDnGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
    //     ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 조회", routingService.getDnGroupByName(name, tenantDbid));
    //     log.debug("getDnGroupByName 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dn-groups/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "DNGroup 생성", description = "DNGroup을 생성합니다.")
    // public ApiResponse<DnGroupSummary> createDnGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DNGroup 생성 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DnGroupRequest.class),
    //                 examples = @ExampleObject(name = "dnGroupCreate", value = "{\"tenantDbid\":101,\"name\":\"DNGROUP_A\",\"description\":\"DN Group\",\"enabled\":true}")
    //             )
    //         )
    //         @Valid @RequestBody DnGroupRequest request) {
    // log.debug("createDnGroup 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 생성", routingService.createDnGroup(request));
    //     log.debug("createDnGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dn-groups/delete")
    // @Operation(summary = "DNGroup 삭제", description = "DNGroup을 삭제합니다.")
    // public ApiResponse<Void> deleteDnGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DNGroup 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnGroupDelete", value = "{\"dbid\":8001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int groupDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("deleteDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
    //     routingService.deleteDnGroup(groupDbid, tenantDbid);
    //     ApiResponse<Void> response = ApiResponse.ok("DNGroup 삭제", null);
    //     log.debug("deleteDnGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/get")
    // @Operation(summary = "DN 조회", description = "DN을 DBID로 조회합니다.")
    // public ApiResponse<DnSummary> getDn(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 조회 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnGet", value = "{\"dbid\":9001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int dnDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
    //     ApiResponse<DnSummary> response = ApiResponse.ok("DN 조회", routingService.getDn(dnDbid, tenantDbid));
    //     log.debug("getDn 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/by-name")
    // @Operation(summary = "DN 조회(이름)", description = "DN을 이름으로 조회합니다.")
    // public ApiResponse<DnSummary> getDnByName(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 조회(이름) 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = NameTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnByName", value = "{\"name\":\"DN_A\",\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody NameTenantRequest request) {
    //     String name = request.name();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getDnByName 요청: name={}, tenantDbid={}", name, tenantDbid);
    //     ApiResponse<DnSummary> response = ApiResponse.ok("DN 조회", routingService.getDnByName(name, tenantDbid));
    //     log.debug("getDnByName 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "DN 생성", description = "DN을 생성합니다.")
    // public ApiResponse<DnSummary> createDn(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 생성 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DnRequest.class),
    //                 examples = @ExampleObject(name = "dnCreate", value = "{\"tenantDbid\":101,\"number\":\"1000\",\"switchDbid\":1,\"type\":\"CFGDN\"}")
    //             )
    //         )
    //         @Valid @RequestBody DnRequest request) {
    // log.debug("createDn 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<DnSummary> response = ApiResponse.ok("DN 생성", routingService.createDn(request));
    //     log.debug("createDn 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/update")
    // @Operation(summary = "DN 수정", description = "DN 정보를 수정합니다.")
    // public ApiResponse<DnSummary> updateDn(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 수정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DnUpdateCommand.class),
    //                 examples = @ExampleObject(name = "dnUpdate", value = "{\"dnDbid\":9001,\"payload\":{\"tenantDbid\":101,\"number\":\"1000\",\"switchDbid\":1,\"type\":\"CFGDN\"}}")
    //             )
    //         )
    //         @Valid @RequestBody DnUpdateCommand command) {
    // log.debug("updateDn 요청: dnDbid={}, payload={}", command.dnDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<DnSummary> response = ApiResponse.ok("DN 수정",
    //         routingService.updateDn(command.dnDbid(), command.payload()));
    //     log.debug("updateDn 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/dial-plan/update")
    // @Operation(summary = "DN DialPlan 설정", description = "DN DialPlan을 설정합니다.")
    // public ApiResponse<Void> updateDnDialPlan(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DialPlan 설정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DnDialPlanCommand.class),
    //                 examples = @ExampleObject(name = "dnDialPlan", value = "{\"dnDbid\":9001,\"payload\":{\"dialPlanDbid\":3001}}")
    //             )
    //         )
    //         @Valid @RequestBody DnDialPlanCommand command) {
    // log.debug("updateDnDialPlan 요청: dnDbid={}, payload={}", command.dnDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.setDnDialPlan(command.dnDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("DN DialPlan 설정", null);
    //     log.debug("updateDnDialPlan 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/tserver-options/update")
    // @Operation(summary = "DN TServer 옵션 설정", description = "DN TServer 옵션을 설정합니다.")
    // public ApiResponse<Void> updateDnTserverOptions(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "TServer 옵션 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DnTServerOptionCommand.class),
    //                 examples = @ExampleObject(name = "dnTserverOption", value = "{\"dnDbid\":9001,\"payload\":{\"options\":{\"key\":\"value\"}}}")
    //             )
    //         )
    //         @RequestBody DnTServerOptionCommand command) {
    // log.debug("updateDnTserverOptions 요청: dnDbid={}, payload={}", command.dnDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.setDnTServerOptions(command.dnDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("DN TServer 옵션 설정", null);
    //     log.debug("updateDnTserverOptions 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/dns/delete")
    // @Operation(summary = "DN 삭제", description = "DN을 삭제합니다.")
    // public ApiResponse<Void> deleteDn(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "DN 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "dnDelete", value = "{\"dbid\":9001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int dnDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("deleteDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
    //     routingService.deleteDn(dnDbid, tenantDbid);
    //     ApiResponse<Void> response = ApiResponse.ok("DN 삭제", null);
    //     log.debug("deleteDn 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions")
    // @Operation(summary = "트랜잭션 목록", description = "트랜잭션 목록을 조회합니다.")
    // public ApiResponse<List<TransactionSummary>> listTransactions(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 조회 요청",
    //             required = false,
    //             content = @Content(
    //                 schema = @Schema(implementation = TenantDbidRequest.class),
    //                 examples = @ExampleObject(name = "transactionList", value = "{\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody TenantDbidRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listTransactions 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<TransactionSummary>> response = ApiResponse.ok("트랜잭션 목록", routingService.listTransactions(tenantDbid));
    //     log.debug("listTransactions 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/transactions/get")
    // @Operation(summary = "트랜잭션 조회", description = "트랜잭션을 DBID로 조회합니다.")
    // public ApiResponse<TransactionSummary> getTransaction(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 조회 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "transactionGet", value = "{\"dbid\":10001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int transactionDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
    //     ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 조회",
    //         routingService.getTransaction(transactionDbid, tenantDbid));
    //     log.debug("getTransaction 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/by-name")
    // @Operation(summary = "트랜잭션 조회(이름)", description = "트랜잭션을 이름으로 조회합니다.")
    // public ApiResponse<TransactionSummary> getTransactionByName(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 조회(이름) 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = NameTenantRequest.class),
    //                 examples = @ExampleObject(name = "transactionByName", value = "{\"name\":\"TRX_A\",\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody NameTenantRequest request) {
    //     String name = request.name();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getTransactionByName 요청: name={}, tenantDbid={}", name, tenantDbid);
    //     ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 조회",
    //         routingService.getTransactionByName(name, tenantDbid));
    //     log.debug("getTransactionByName 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "트랜잭션 생성", description = "트랜잭션을 생성합니다.")
    // public ApiResponse<TransactionSummary> createTransaction(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 생성 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionRequest.class),
    //                 examples = @ExampleObject(name = "transactionCreate", value = "{\"tenantDbid\":101,\"name\":\"TRX_A\",\"description\":\"Transaction\",\"enabled\":true}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionRequest request) {
    // log.debug("createTransaction 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 생성", routingService.createTransaction(request));
    //     log.debug("createTransaction 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/update")
    // @Operation(summary = "트랜잭션 수정", description = "트랜잭션 정보를 수정합니다.")
    // public ApiResponse<TransactionSummary> updateTransaction(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 수정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionUpdateCommand.class),
    //                 examples = @ExampleObject(name = "transactionUpdate", value = "{\"transactionDbid\":10001,\"payload\":{\"tenantDbid\":101,\"name\":\"TRX_A\",\"description\":\"Transaction\",\"enabled\":true}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionUpdateCommand command) {
    // log.debug("updateTransaction 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 수정",
    //         routingService.updateTransaction(command.transactionDbid(), command.payload()));
    //     log.debug("updateTransaction 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/delete")
    // @Operation(summary = "트랜잭션 삭제", description = "트랜잭션을 삭제합니다.")
    // public ApiResponse<Void> deleteTransaction(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "트랜잭션 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "transactionDelete", value = "{\"dbid\":10001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int transactionDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("deleteTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
    //     routingService.deleteTransaction(transactionDbid, tenantDbid);
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 삭제", null);
    //     log.debug("deleteTransaction 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/sections/add")
    // @Operation(summary = "트랜잭션 섹션 추가", description = "트랜잭션 섹션을 추가합니다.")
    // public ApiResponse<Void> addTransactionSection(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "섹션 추가 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionSectionCommand.class),
    //                 examples = @ExampleObject(name = "transactionSectionAdd", value = "{\"transactionDbid\":10001,\"payload\":{\"sectionName\":\"S1\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionSectionCommand command) {
    // log.debug("addTransactionSection 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.addTransactionSection(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 추가", null);
    //     log.debug("addTransactionSection 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/sections/update")
    // @Operation(summary = "트랜잭션 섹션 수정", description = "트랜잭션 섹션을 수정합니다.")
    // public ApiResponse<Void> updateTransactionSection(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "섹션 수정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionSectionCommand.class),
    //                 examples = @ExampleObject(name = "transactionSectionUpdate", value = "{\"transactionDbid\":10001,\"payload\":{\"sectionName\":\"S1\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionSectionCommand command) {
    // log.debug("updateTransactionSection 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.modifyTransactionSection(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 수정", null);
    //     log.debug("updateTransactionSection 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/sections/delete")
    // @Operation(summary = "트랜잭션 섹션 삭제", description = "트랜잭션 섹션을 삭제합니다.")
    // public ApiResponse<Void> deleteTransactionSection(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "섹션 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionSectionCommand.class),
    //                 examples = @ExampleObject(name = "transactionSectionDelete", value = "{\"transactionDbid\":10001,\"payload\":{\"sectionName\":\"S1\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionSectionCommand command) {
    // log.debug("deleteTransactionSection 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.removeTransactionSection(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 삭제", null);
    //     log.debug("deleteTransactionSection 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/options/add")
    // @Operation(summary = "트랜잭션 옵션 추가", description = "트랜잭션 옵션을 추가합니다.")
    // public ApiResponse<Void> addTransactionOption(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "옵션 추가 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionOptionCommand.class),
    //                 examples = @ExampleObject(name = "transactionOptionAdd", value = "{\"transactionDbid\":10001,\"payload\":{\"optionName\":\"opt1\",\"optionValue\":\"value\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionOptionCommand command) {
    // log.debug("addTransactionOption 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.addTransactionOption(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 추가", null);
    //     log.debug("addTransactionOption 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/options/update")
    // @Operation(summary = "트랜잭션 옵션 수정", description = "트랜잭션 옵션을 수정합니다.")
    // public ApiResponse<Void> updateTransactionOption(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "옵션 수정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionOptionCommand.class),
    //                 examples = @ExampleObject(name = "transactionOptionUpdate", value = "{\"transactionDbid\":10001,\"payload\":{\"optionName\":\"opt1\",\"optionValue\":\"value\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionOptionCommand command) {
    // log.debug("updateTransactionOption 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.modifyTransactionOption(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 수정", null);
    //     log.debug("updateTransactionOption 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/options/delete")
    // @Operation(summary = "트랜잭션 옵션 삭제", description = "트랜잭션 옵션을 삭제합니다.")
    // public ApiResponse<Void> deleteTransactionOption(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "옵션 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionOptionCommand.class),
    //                 examples = @ExampleObject(name = "transactionOptionDelete", value = "{\"transactionDbid\":10001,\"payload\":{\"optionName\":\"opt1\"}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionOptionCommand command) {
    // log.debug("deleteTransactionOption 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.removeTransactionOption(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 삭제", null);
    //     log.debug("deleteTransactionOption 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/transactions/options/save")
    // @Operation(summary = "트랜잭션 옵션 저장", description = "트랜잭션 옵션을 저장합니다.")
    // public ApiResponse<Void> saveTransactionOptions(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "옵션 저장 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = TransactionOptionsSaveCommand.class),
    //                 examples = @ExampleObject(name = "transactionOptionSave", value = "{\"transactionDbid\":10001,\"payload\":{\"options\":[{\"optionName\":\"opt1\",\"optionValue\":\"value\"}]}}")
    //             )
    //         )
    //         @Valid @RequestBody TransactionOptionsSaveCommand command) {
    // log.debug("saveTransactionOptions 요청: transactionDbid={}, payload={}", command.transactionDbid(), SensitiveLogMasker.masked(command.payload()));
    //     routingService.saveTransactionOptions(command.transactionDbid(), command.payload());
    //     ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 저장", null);
    //     log.debug("saveTransactionOptions 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places")
    // @Operation(summary = "Place 목록", description = "Place 목록을 조회합니다.")
    // public ApiResponse<List<PlaceSummary>> listPlaces(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 조회 요청",
    //             required = false,
    //             content = @Content(
    //                 schema = @Schema(implementation = TenantDbidRequest.class),
    //                 examples = @ExampleObject(name = "placeList", value = "{\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody TenantDbidRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listPlaces 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<PlaceSummary>> response = ApiResponse.ok("Place 목록", routingService.listPlaces(tenantDbid));
    //     log.debug("listPlaces 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/place-groups")
    // @Operation(summary = "PlaceGroup 목록", description = "PlaceGroup 목록을 조회합니다.")
    // public ApiResponse<List<PlaceGroupSummary>> listPlaceGroups(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "PlaceGroup 조회 요청",
    //             required = false,
    //             content = @Content(
    //                 schema = @Schema(implementation = TenantDbidRequest.class),
    //                 examples = @ExampleObject(name = "placeGroupList", value = "{\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody TenantDbidRequest request) {
    //     Integer tenantDbid = request == null ? null : request.tenantDbid();
    //     log.debug("listPlaceGroups 요청: tenantDbid={}", tenantDbid);
    //     ApiResponse<List<PlaceGroupSummary>> response = ApiResponse.ok("PlaceGroup 목록", routingService.listPlaceGroups(tenantDbid));
    //     log.debug("listPlaceGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
    //     return response;
    // }

    // @PostMapping("/place-groups/get")
    // @Operation(summary = "PlaceGroup 조회", description = "PlaceGroup을 DBID로 조회합니다.")
    // public ApiResponse<PlaceGroupSummary> getPlaceGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "PlaceGroup 조회 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeGroupGet", value = "{\"dbid\":11001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int groupDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getPlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
    //     ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 조회", routingService.getPlaceGroup(groupDbid, tenantDbid));
    //     log.debug("getPlaceGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/place-groups/by-name")
    // @Operation(summary = "PlaceGroup 조회(이름)", description = "PlaceGroup을 이름으로 조회합니다.")
    // public ApiResponse<PlaceGroupSummary> getPlaceGroupByName(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "PlaceGroup 조회(이름) 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = NameTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeGroupByName", value = "{\"name\":\"PLACE_GROUP_A\",\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody NameTenantRequest request) {
    //     String name = request.name();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getPlaceGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
    //     ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 조회", routingService.getPlaceGroupByName(name, tenantDbid));
    //     log.debug("getPlaceGroupByName 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/place-groups/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "PlaceGroup 생성", description = "PlaceGroup을 생성합니다.")
    // public ApiResponse<PlaceGroupSummary> createPlaceGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "PlaceGroup 생성 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = PlaceGroupRequest.class),
    //                 examples = @ExampleObject(name = "placeGroupCreate", value = "{\"tenantDbid\":101,\"name\":\"PLACE_GROUP_A\",\"description\":\"Place Group\",\"enabled\":true}")
    //             )
    //         )
    //         @Valid @RequestBody PlaceGroupRequest request) {
    // log.debug("createPlaceGroup 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 생성", routingService.createPlaceGroup(request));
    //     log.debug("createPlaceGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/place-groups/delete")
    // @Operation(summary = "PlaceGroup 삭제", description = "PlaceGroup을 삭제합니다.")
    // public ApiResponse<Void> deletePlaceGroup(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "PlaceGroup 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeGroupDelete", value = "{\"dbid\":11001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int groupDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("deletePlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
    //     routingService.deletePlaceGroup(groupDbid, tenantDbid);
    //     ApiResponse<Void> response = ApiResponse.ok("PlaceGroup 삭제", null);
    //     log.debug("deletePlaceGroup 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places/get")
    // @Operation(summary = "Place 조회", description = "Place를 DBID로 조회합니다.")
    // public ApiResponse<PlaceSummary> getPlace(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 조회 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeGet", value = "{\"dbid\":12001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int placeDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getPlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
    //     ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 조회", routingService.getPlace(placeDbid, tenantDbid));
    //     log.debug("getPlace 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places/by-name")
    // @Operation(summary = "Place 조회(이름)", description = "Place를 이름으로 조회합니다.")
    // public ApiResponse<PlaceSummary> getPlaceByName(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 조회(이름) 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = NameTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeByName", value = "{\"name\":\"PLACE_A\",\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody NameTenantRequest request) {
    //     String name = request.name();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("getPlaceByName 요청: name={}, tenantDbid={}", name, tenantDbid);
    //     ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 조회", routingService.getPlaceByName(name, tenantDbid));
    //     log.debug("getPlaceByName 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places/create")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Operation(summary = "Place 생성", description = "Place를 생성합니다.")
    // public ApiResponse<PlaceSummary> createPlace(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 생성 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = PlaceRequest.class),
    //                 examples = @ExampleObject(name = "placeCreate", value = "{\"tenantDbid\":101,\"name\":\"PLACE_A\",\"description\":\"Place\",\"enabled\":true}")
    //             )
    //         )
    //         @Valid @RequestBody PlaceRequest request) {
    // log.debug("createPlace 요청: {}", SensitiveLogMasker.masked(request));
    //     ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 생성", routingService.createPlace(request));
    //     log.debug("createPlace 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places/update")
    // @Operation(summary = "Place 수정", description = "Place 정보를 수정합니다.")
    // public ApiResponse<PlaceSummary> updatePlace(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 수정 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = PlaceUpdateCommand.class),
    //                 examples = @ExampleObject(name = "placeUpdate", value = "{\"placeDbid\":12001,\"payload\":{\"tenantDbid\":101,\"name\":\"PLACE_A\",\"description\":\"Place\",\"enabled\":true}}")
    //             )
    //         )
    //         @Valid @RequestBody PlaceUpdateCommand command) {
    // log.debug("updatePlace 요청: placeDbid={}, payload={}", command.placeDbid(), SensitiveLogMasker.masked(command.payload()));
    //     ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 수정",
    //         routingService.updatePlace(command.placeDbid(), command.payload()));
    //     log.debug("updatePlace 응답: {}", response);
    //     return response;
    // }

    // @PostMapping("/places/delete")
    // @Operation(summary = "Place 삭제", description = "Place를 삭제합니다.")
    // public ApiResponse<Void> deletePlace(
    //         @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //             description = "Place 삭제 요청",
    //             required = true,
    //             content = @Content(
    //                 schema = @Schema(implementation = DbidTenantRequest.class),
    //                 examples = @ExampleObject(name = "placeDelete", value = "{\"dbid\":12001,\"tenantDbid\":101}")
    //             )
    //         )
    //         @RequestBody DbidTenantRequest request) {
    //     int placeDbid = request.dbid();
    //     Integer tenantDbid = request.tenantDbid();
    //     log.debug("deletePlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
    //     routingService.deletePlace(placeDbid, tenantDbid);
    //     ApiResponse<Void> response = ApiResponse.ok("Place 삭제", null);
    //     log.debug("deletePlace 응답: {}", response);
    //     return response;
    // }
}
