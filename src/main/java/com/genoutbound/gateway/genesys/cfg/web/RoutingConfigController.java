package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.DnDialPlanRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnTServerOptionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionsSaveRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSectionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSummary;
import com.genoutbound.gateway.genesys.cfg.service.RoutingConfigService;
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
 * DN/Transaction/Place 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class RoutingConfigController {

    private static final Logger log = LoggerFactory.getLogger(RoutingConfigController.class);
    private final RoutingConfigService routingService;

    public RoutingConfigController(RoutingConfigService routingService) {
        this.routingService = routingService;
    }

    @GetMapping("/dns")
    @Operation(summary = "DN 목록", description = "DN 목록을 조회합니다.")
    public ApiResponse<List<DnSummary>> listDns(
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listDns 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<DnSummary>> response = ApiResponse.ok("DN 목록", routingService.listDns(tenantDbid));
        log.debug("listDns 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/dn-groups")
    @Operation(summary = "DNGroup 목록", description = "DNGroup 목록을 조회합니다.")
    public ApiResponse<List<DnGroupSummary>> listDnGroups(
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listDnGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<DnGroupSummary>> response = ApiResponse.ok("DNGroup 목록", routingService.listDnGroups(tenantDbid));
        log.debug("listDnGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/dn-groups/{groupDbid}")
    @Operation(summary = "DNGroup 조회", description = "DNGroup을 DBID로 조회합니다.")
    public ApiResponse<DnGroupSummary> getDnGroup(
            @Parameter(description = "DNGroup DBID", example = "8001")
            @PathVariable int groupDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 조회", routingService.getDnGroup(groupDbid, tenantDbid));
        log.debug("getDnGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/dn-groups/by-name")
    @Operation(summary = "DNGroup 조회(이름)", description = "DNGroup을 이름으로 조회합니다.")
    public ApiResponse<DnGroupSummary> getDnGroupByName(
            @Parameter(description = "DNGroup 이름", example = "DNGROUP_A")
            @RequestParam String name,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getDnGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 조회", routingService.getDnGroupByName(name, tenantDbid));
        log.debug("getDnGroupByName 응답: {}", response);
        return response;
    }

    @PostMapping("/dn-groups")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "DNGroup 생성", description = "DNGroup을 생성합니다.")
    public ApiResponse<DnGroupSummary> createDnGroup(
            @Parameter(description = "DNGroup 생성 요청")
            @Valid @RequestBody DnGroupRequest request) {
        log.debug("createDnGroup 요청: {}", request);
        ApiResponse<DnGroupSummary> response = ApiResponse.ok("DNGroup 생성", routingService.createDnGroup(request));
        log.debug("createDnGroup 응답: {}", response);
        return response;
    }

    @DeleteMapping("/dn-groups/{groupDbid}")
    @Operation(summary = "DNGroup 삭제", description = "DNGroup을 삭제합니다.")
    public ApiResponse<Void> deleteDnGroup(
            @Parameter(description = "DNGroup DBID", example = "8001")
            @PathVariable int groupDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        routingService.deleteDnGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("DNGroup 삭제", null);
        log.debug("deleteDnGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/dns/{dnDbid}")
    @Operation(summary = "DN 조회", description = "DN을 DBID로 조회합니다.")
    public ApiResponse<DnSummary> getDn(
            @Parameter(description = "DN DBID", example = "9001")
            @PathVariable int dnDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
        ApiResponse<DnSummary> response = ApiResponse.ok("DN 조회", routingService.getDn(dnDbid, tenantDbid));
        log.debug("getDn 응답: {}", response);
        return response;
    }

    @GetMapping("/dns/by-name")
    @Operation(summary = "DN 조회(이름)", description = "DN을 이름으로 조회합니다.")
    public ApiResponse<DnSummary> getDnByName(
            @Parameter(description = "DN 이름", example = "DN_A")
            @RequestParam String name,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getDnByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<DnSummary> response = ApiResponse.ok("DN 조회", routingService.getDnByName(name, tenantDbid));
        log.debug("getDnByName 응답: {}", response);
        return response;
    }

    @PostMapping("/dns")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "DN 생성", description = "DN을 생성합니다.")
    public ApiResponse<DnSummary> createDn(
            @Parameter(description = "DN 생성 요청")
            @Valid @RequestBody DnRequest request) {
        log.debug("createDn 요청: {}", request);
        ApiResponse<DnSummary> response = ApiResponse.ok("DN 생성", routingService.createDn(request));
        log.debug("createDn 응답: {}", response);
        return response;
    }

    @PutMapping("/dns/{dnDbid}")
    @Operation(summary = "DN 수정", description = "DN 정보를 수정합니다.")
    public ApiResponse<DnSummary> updateDn(
            @Parameter(description = "DN DBID", example = "9001")
            @PathVariable int dnDbid,
            @Parameter(description = "DN 수정 요청")
            @Valid @RequestBody DnRequest request) {
        log.debug("updateDn 요청: dnDbid={}, request={}", dnDbid, request);
        ApiResponse<DnSummary> response = ApiResponse.ok("DN 수정", routingService.updateDn(dnDbid, request));
        log.debug("updateDn 응답: {}", response);
        return response;
    }

    @PutMapping("/dns/{dnDbid}/dial-plan")
    @Operation(summary = "DN DialPlan 설정", description = "DN DialPlan을 설정합니다.")
    public ApiResponse<Void> updateDnDialPlan(
            @Parameter(description = "DN DBID", example = "9001")
            @PathVariable int dnDbid,
            @Parameter(description = "DialPlan 설정 요청")
            @Valid @RequestBody DnDialPlanRequest request) {
        log.debug("updateDnDialPlan 요청: dnDbid={}, request={}", dnDbid, request);
        routingService.setDnDialPlan(dnDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("DN DialPlan 설정", null);
        log.debug("updateDnDialPlan 응답: {}", response);
        return response;
    }

    @PutMapping("/dns/{dnDbid}/tserver-options")
    @Operation(summary = "DN TServer 옵션 설정", description = "DN TServer 옵션을 설정합니다.")
    public ApiResponse<Void> updateDnTserverOptions(
            @Parameter(description = "DN DBID", example = "9001")
            @PathVariable int dnDbid,
            @Parameter(description = "TServer 옵션 요청")
            @RequestBody DnTServerOptionRequest request) {
        log.debug("updateDnTserverOptions 요청: dnDbid={}, request={}", dnDbid, request);
        routingService.setDnTServerOptions(dnDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("DN TServer 옵션 설정", null);
        log.debug("updateDnTserverOptions 응답: {}", response);
        return response;
    }

    @DeleteMapping("/dns/{dnDbid}")
    @Operation(summary = "DN 삭제", description = "DN을 삭제합니다.")
    public ApiResponse<Void> deleteDn(
            @Parameter(description = "DN DBID", example = "9001")
            @PathVariable int dnDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
        routingService.deleteDn(dnDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("DN 삭제", null);
        log.debug("deleteDn 응답: {}", response);
        return response;
    }

    @GetMapping("/transactions")
    @Operation(summary = "트랜잭션 목록", description = "트랜잭션 목록을 조회합니다.")
    public ApiResponse<List<TransactionSummary>> listTransactions(
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listTransactions 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TransactionSummary>> response = ApiResponse.ok("트랜잭션 목록", routingService.listTransactions(tenantDbid));
        log.debug("listTransactions 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/transactions/{transactionDbid}")
    @Operation(summary = "트랜잭션 조회", description = "트랜잭션을 DBID로 조회합니다.")
    public ApiResponse<TransactionSummary> getTransaction(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
        ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 조회",
            routingService.getTransaction(transactionDbid, tenantDbid));
        log.debug("getTransaction 응답: {}", response);
        return response;
    }

    @GetMapping("/transactions/by-name")
    @Operation(summary = "트랜잭션 조회(이름)", description = "트랜잭션을 이름으로 조회합니다.")
    public ApiResponse<TransactionSummary> getTransactionByName(
            @Parameter(description = "트랜잭션 이름", example = "TRX_A")
            @RequestParam String name,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getTransactionByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 조회",
            routingService.getTransactionByName(name, tenantDbid));
        log.debug("getTransactionByName 응답: {}", response);
        return response;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "트랜잭션 생성", description = "트랜잭션을 생성합니다.")
    public ApiResponse<TransactionSummary> createTransaction(
            @Parameter(description = "트랜잭션 생성 요청")
            @Valid @RequestBody TransactionRequest request) {
        log.debug("createTransaction 요청: {}", request);
        ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 생성", routingService.createTransaction(request));
        log.debug("createTransaction 응답: {}", response);
        return response;
    }

    @PutMapping("/transactions/{transactionDbid}")
    @Operation(summary = "트랜잭션 수정", description = "트랜잭션 정보를 수정합니다.")
    public ApiResponse<TransactionSummary> updateTransaction(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "트랜잭션 수정 요청")
            @Valid @RequestBody TransactionRequest request) {
        log.debug("updateTransaction 요청: transactionDbid={}, request={}", transactionDbid, request);
        ApiResponse<TransactionSummary> response = ApiResponse.ok("트랜잭션 수정",
            routingService.updateTransaction(transactionDbid, request));
        log.debug("updateTransaction 응답: {}", response);
        return response;
    }

    @DeleteMapping("/transactions/{transactionDbid}")
    @Operation(summary = "트랜잭션 삭제", description = "트랜잭션을 삭제합니다.")
    public ApiResponse<Void> deleteTransaction(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
        routingService.deleteTransaction(transactionDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 삭제", null);
        log.debug("deleteTransaction 응답: {}", response);
        return response;
    }

    @PostMapping("/transactions/{transactionDbid}/sections")
    @Operation(summary = "트랜잭션 섹션 추가", description = "트랜잭션 섹션을 추가합니다.")
    public ApiResponse<Void> addTransactionSection(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "섹션 추가 요청")
            @Valid @RequestBody TransactionSectionRequest request) {
        log.debug("addTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.addTransactionSection(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 추가", null);
        log.debug("addTransactionSection 응답: {}", response);
        return response;
    }

    @PutMapping("/transactions/{transactionDbid}/sections")
    @Operation(summary = "트랜잭션 섹션 수정", description = "트랜잭션 섹션을 수정합니다.")
    public ApiResponse<Void> updateTransactionSection(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "섹션 수정 요청")
            @Valid @RequestBody TransactionSectionRequest request) {
        log.debug("updateTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.modifyTransactionSection(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 수정", null);
        log.debug("updateTransactionSection 응답: {}", response);
        return response;
    }

    @DeleteMapping("/transactions/{transactionDbid}/sections")
    @Operation(summary = "트랜잭션 섹션 삭제", description = "트랜잭션 섹션을 삭제합니다.")
    public ApiResponse<Void> deleteTransactionSection(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "섹션 삭제 요청")
            @Valid @RequestBody TransactionSectionRequest request) {
        log.debug("deleteTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.removeTransactionSection(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 섹션 삭제", null);
        log.debug("deleteTransactionSection 응답: {}", response);
        return response;
    }

    @PostMapping("/transactions/{transactionDbid}/options")
    @Operation(summary = "트랜잭션 옵션 추가", description = "트랜잭션 옵션을 추가합니다.")
    public ApiResponse<Void> addTransactionOption(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "옵션 추가 요청")
            @Valid @RequestBody TransactionOptionRequest request) {
        log.debug("addTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.addTransactionOption(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 추가", null);
        log.debug("addTransactionOption 응답: {}", response);
        return response;
    }

    @PutMapping("/transactions/{transactionDbid}/options")
    @Operation(summary = "트랜잭션 옵션 수정", description = "트랜잭션 옵션을 수정합니다.")
    public ApiResponse<Void> updateTransactionOption(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "옵션 수정 요청")
            @Valid @RequestBody TransactionOptionRequest request) {
        log.debug("updateTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.modifyTransactionOption(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 수정", null);
        log.debug("updateTransactionOption 응답: {}", response);
        return response;
    }

    @DeleteMapping("/transactions/{transactionDbid}/options")
    @Operation(summary = "트랜잭션 옵션 삭제", description = "트랜잭션 옵션을 삭제합니다.")
    public ApiResponse<Void> deleteTransactionOption(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "옵션 삭제 요청")
            @Valid @RequestBody TransactionOptionRequest request) {
        log.debug("deleteTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.removeTransactionOption(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 삭제", null);
        log.debug("deleteTransactionOption 응답: {}", response);
        return response;
    }

    @PutMapping("/transactions/{transactionDbid}/options/save")
    @Operation(summary = "트랜잭션 옵션 저장", description = "트랜잭션 옵션을 저장합니다.")
    public ApiResponse<Void> saveTransactionOptions(
            @Parameter(description = "트랜잭션 DBID", example = "10001")
            @PathVariable int transactionDbid,
            @Parameter(description = "옵션 저장 요청")
            @Valid @RequestBody TransactionOptionsSaveRequest request) {
        log.debug("saveTransactionOptions 요청: transactionDbid={}, request={}", transactionDbid, request);
        routingService.saveTransactionOptions(transactionDbid, request);
        ApiResponse<Void> response = ApiResponse.ok("트랜잭션 옵션 저장", null);
        log.debug("saveTransactionOptions 응답: {}", response);
        return response;
    }

    @GetMapping("/places")
    @Operation(summary = "Place 목록", description = "Place 목록을 조회합니다.")
    public ApiResponse<List<PlaceSummary>> listPlaces(
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listPlaces 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<PlaceSummary>> response = ApiResponse.ok("Place 목록", routingService.listPlaces(tenantDbid));
        log.debug("listPlaces 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/place-groups")
    @Operation(summary = "PlaceGroup 목록", description = "PlaceGroup 목록을 조회합니다.")
    public ApiResponse<List<PlaceGroupSummary>> listPlaceGroups(
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listPlaceGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<PlaceGroupSummary>> response = ApiResponse.ok("PlaceGroup 목록", routingService.listPlaceGroups(tenantDbid));
        log.debug("listPlaceGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/place-groups/{groupDbid}")
    @Operation(summary = "PlaceGroup 조회", description = "PlaceGroup을 DBID로 조회합니다.")
    public ApiResponse<PlaceGroupSummary> getPlaceGroup(
            @Parameter(description = "PlaceGroup DBID", example = "11001")
            @PathVariable int groupDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 조회", routingService.getPlaceGroup(groupDbid, tenantDbid));
        log.debug("getPlaceGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/place-groups/by-name")
    @Operation(summary = "PlaceGroup 조회(이름)", description = "PlaceGroup을 이름으로 조회합니다.")
    public ApiResponse<PlaceGroupSummary> getPlaceGroupByName(
            @Parameter(description = "PlaceGroup 이름", example = "PLACE_GROUP_A")
            @RequestParam String name,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPlaceGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 조회", routingService.getPlaceGroupByName(name, tenantDbid));
        log.debug("getPlaceGroupByName 응답: {}", response);
        return response;
    }

    @PostMapping("/place-groups")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "PlaceGroup 생성", description = "PlaceGroup을 생성합니다.")
    public ApiResponse<PlaceGroupSummary> createPlaceGroup(
            @Parameter(description = "PlaceGroup 생성 요청")
            @Valid @RequestBody PlaceGroupRequest request) {
        log.debug("createPlaceGroup 요청: {}", request);
        ApiResponse<PlaceGroupSummary> response = ApiResponse.ok("PlaceGroup 생성", routingService.createPlaceGroup(request));
        log.debug("createPlaceGroup 응답: {}", response);
        return response;
    }

    @DeleteMapping("/place-groups/{groupDbid}")
    @Operation(summary = "PlaceGroup 삭제", description = "PlaceGroup을 삭제합니다.")
    public ApiResponse<Void> deletePlaceGroup(
            @Parameter(description = "PlaceGroup DBID", example = "11001")
            @PathVariable int groupDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deletePlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        routingService.deletePlaceGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("PlaceGroup 삭제", null);
        log.debug("deletePlaceGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/places/{placeDbid}")
    @Operation(summary = "Place 조회", description = "Place를 DBID로 조회합니다.")
    public ApiResponse<PlaceSummary> getPlace(
            @Parameter(description = "Place DBID", example = "12001")
            @PathVariable int placeDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
        ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 조회", routingService.getPlace(placeDbid, tenantDbid));
        log.debug("getPlace 응답: {}", response);
        return response;
    }

    @GetMapping("/places/by-name")
    @Operation(summary = "Place 조회(이름)", description = "Place를 이름으로 조회합니다.")
    public ApiResponse<PlaceSummary> getPlaceByName(
            @Parameter(description = "Place 이름", example = "PLACE_A")
            @RequestParam String name,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getPlaceByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 조회", routingService.getPlaceByName(name, tenantDbid));
        log.debug("getPlaceByName 응답: {}", response);
        return response;
    }

    @PostMapping("/places")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place 생성", description = "Place를 생성합니다.")
    public ApiResponse<PlaceSummary> createPlace(
            @Parameter(description = "Place 생성 요청")
            @Valid @RequestBody PlaceRequest request) {
        log.debug("createPlace 요청: {}", request);
        ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 생성", routingService.createPlace(request));
        log.debug("createPlace 응답: {}", response);
        return response;
    }

    @PutMapping("/places/{placeDbid}")
    @Operation(summary = "Place 수정", description = "Place 정보를 수정합니다.")
    public ApiResponse<PlaceSummary> updatePlace(
            @Parameter(description = "Place DBID", example = "12001")
            @PathVariable int placeDbid,
            @Parameter(description = "Place 수정 요청")
            @Valid @RequestBody PlaceRequest request) {
        log.debug("updatePlace 요청: placeDbid={}, request={}", placeDbid, request);
        ApiResponse<PlaceSummary> response = ApiResponse.ok("Place 수정", routingService.updatePlace(placeDbid, request));
        log.debug("updatePlace 응답: {}", response);
        return response;
    }

    @DeleteMapping("/places/{placeDbid}")
    @Operation(summary = "Place 삭제", description = "Place를 삭제합니다.")
    public ApiResponse<Void> deletePlace(
            @Parameter(description = "Place DBID", example = "12001")
            @PathVariable int placeDbid,
            @Parameter(description = "테넌트 DBID", example = "101")
            @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deletePlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
        routingService.deletePlace(placeDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Place 삭제", null);
        log.debug("deletePlace 응답: {}", response);
        return response;
    }
}
