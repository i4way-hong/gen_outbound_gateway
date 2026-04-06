package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateResponse;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 아웃바운드(콜링리스트/필터/캠페인 등) 관련 API를 제공합니다.
 */
@ConfigurationApiController
@ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "success",
                value = "{\"success\":true,\"message\":\"요청 성공\",\"data\":{},\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201",
        description = "생성 성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "created",
                value = "{\"success\":true,\"message\":\"생성 성공\",\"data\":{},\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "잘못된 요청",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "bad-request",
                value = "{\"success\":false,\"message\":\"요청 값이 올바르지 않습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "대상 없음",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "filter-not-found",
                    value = "{\"success\":false,\"message\":\"Filter를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "calling-list-not-found",
                    value = "{\"success\":false,\"message\":\"콜링리스트를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-group-not-found",
                    value = "{\"success\":false,\"message\":\"CampaignGroup을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-not-found",
                    value = "{\"success\":false,\"message\":\"캠페인을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "table-access-not-found",
                    value = "{\"success\":false,\"message\":\"TableAccess를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "treatment-not-found",
                    value = "{\"success\":false,\"message\":\"Treatment를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "중복 데이터",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "filter-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 Filter입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "calling-list-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 콜링리스트입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-group-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 CampaignGroup입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 캠페인입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "서버 오류",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "server-error",
                value = "{\"success\":false,\"message\":\"알 수 없는 오류가 발생했습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "503",
        description = "Genesys 연동 실패",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "genesys-disabled",
                    value = "{\"success\":false,\"message\":\"Genesys 설정이 비활성화되어 있습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "genesys-request-failed",
                    value = "{\"success\":false,\"message\":\"요청 처리 중 오류가 발생했습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    )
})
public class OutboundConfigController {

    private static final Logger log = LoggerFactory.getLogger(OutboundConfigController.class);
    private final OutboundConfigService outboundService;

    public OutboundConfigController(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    @PostMapping("/calling-lists")
    @Operation(summary = "콜링리스트 목록", description = "콜링리스트 목록을 조회합니다.")
    public ApiResponse<List<CallingListDetailSummary>> listCallingLists(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "callingListList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCallingLists 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CallingListDetailSummary>> response = ApiResponse.ok("콜링리스트 목록",
            outboundService.listCallingLists(tenantDbid));
        log.debug("listCallingLists 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/filters")
    @Operation(summary = "Filter 목록", description = "Filter 목록을 조회합니다.")
    public ApiResponse<List<FilterSummary>> listFilters(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "filterList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listFilters 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FilterSummary>> response = ApiResponse.ok("Filter 목록", outboundService.listFilters(tenantDbid));
        log.debug("listFilters 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/filters/get")
    @Operation(summary = "Filter 조회", description = "Filter를 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "filterResponse",
                    value = "{\"success\":true,\"message\":\"Filter 조회\",\"data\":{\"dbid\":106,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<FilterSummary> getFilter(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "filterGet", value = "{\"dbid\":4001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int filterDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilter(filterDbid, tenantDbid));
        log.debug("getFilter 응답: {}", response);
        return response;
    }

    @PostMapping("/filters/by-name")
    @Operation(summary = "Filter 조회(이름)", description = "Filter를 이름으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "filterByNameResponse",
                    value = "{\"success\":true,\"message\":\"Filter 조회\",\"data\":{\"dbid\":106,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<FilterSummary> getFilterByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "filterByName", value = "{\"name\":\"FILTER_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFilterByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilterByName(name, tenantDbid));
        log.debug("getFilterByName 응답: {}", response);
        return response;
    }

    @PostMapping("/formats")
    @Operation(summary = "Format 목록", description = "Format 목록을 조회합니다.")
    public ApiResponse<List<FormatSummary>> listFormats(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Format 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "formatList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listFormats 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FormatSummary>> response = ApiResponse.ok("Format 목록", outboundService.listFormats(tenantDbid));
        log.debug("listFormats 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/formats/get")
    @Operation(summary = "Format 조회", description = "Format을 DBID로 조회합니다.")
    public ApiResponse<FormatSummary> getFormat(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Format 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "formatGet", value = "{\"dbid\":12001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int formatDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFormat 요청: formatDbid={}, tenantDbid={}", formatDbid, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회", outboundService.getFormat(formatDbid, tenantDbid));
        log.debug("getFormat 응답: {}", response);
        return response;
    }

    @PostMapping("/formats/by-name")
    @Operation(summary = "Format 조회(이름)", description = "Format을 이름으로 조회합니다.")
    public ApiResponse<FormatSummary> getFormatByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Format 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "formatByName", value = "{\"name\":\"FORMAT_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFormatByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회", outboundService.getFormatByName(name, tenantDbid));
        log.debug("getFormatByName 응답: {}", response);
        return response;
    }

    @PostMapping("/filters/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Filter 생성", description = "Filter를 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "filterCreated",
                    value = "{\"success\":true,\"message\":\"Filter 조회\",\"data\":{\"dbid\":106,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<FilterSummary> createFilter(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = FilterRequest.class),
                examples = @ExampleObject(
                    name = "filterCreate",
                    value = "{\"tenantDbid\":1,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"formatDbid\":104,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody FilterRequest request) {
        log.debug("createFilter 요청: {}", request);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 생성", outboundService.createFilter(request));
        log.debug("createFilter 응답: {}", response);
        return response;
    }

    @PostMapping("/filters/update")
    @Operation(summary = "Filter 수정", description = "Filter 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "filterUpdated",
                    value = "{\"success\":true,\"message\":\"Filter 수정\",\"data\":{\"dbid\":106,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<FilterSummary> updateFilter(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = FilterUpdateCommand.class),
                examples = @ExampleObject(
                    name = "filterUpdate",
                    value = "{\"filterDbid\":4001,\"payload\":{\"tenantDbid\":1,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터(수정)\",\"formatDbid\":104,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true}}"
                )
            )
        )
        @Valid @RequestBody FilterUpdateCommand command) {
        log.debug("updateFilter 요청: filterDbid={}, request={}", command.filterDbid(), command.payload());
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 수정",
            outboundService.updateFilter(command.filterDbid(), command.payload()));
        log.debug("updateFilter 응답: {}", response);
        return response;
    }

    @PostMapping("/filters/delete")
    @Operation(summary = "Filter 삭제", description = "Filter를 삭제합니다.")
    public ApiResponse<Void> deleteFilter(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "filterDelete", value = "{\"dbid\":4001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int filterDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        outboundService.deleteFilter(filterDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Filter 삭제", null);
        log.debug("deleteFilter 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists/get")
    @Operation(summary = "콜링리스트 조회", description = "콜링리스트를 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "callingListResponse",
                    value = "{\"success\":true,\"message\":\"콜링리스트 조회\",\"data\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<CallingListDetailSummary> getCallingList(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "callingListGet", value = "{\"dbid\":5001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int callingListDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingList(callingListDbid, tenantDbid));
        log.debug("getCallingList 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists/by-name")
    @Operation(summary = "콜링리스트 조회(이름)", description = "콜링리스트를 이름으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "callingListByNameResponse",
                    value = "{\"success\":true,\"message\":\"콜링리스트 조회\",\"data\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<CallingListDetailSummary> getCallingListByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "callingListByName", value = "{\"name\":\"LIST_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCallingListByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingListByName(name, tenantDbid));
        log.debug("getCallingListByName 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "콜링리스트 생성", description = "콜링리스트를 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "callingListCreated",
                    value = "{\"success\":true,\"message\":\"콜링리스트 조회\",\"data\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<CallingListDetailSummary> createCallingList(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CallingListDetailRequest.class),
                examples = @ExampleObject(
                    name = "callingListDetail",
                    value = "{\"tenantDbid\":1,\"name\":\"CallingList_20260305_002\",\"description\":\"콜링리스트 설명\",\"filterDbid\":114,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":102,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}"
                )
            )
        )
        @Valid @RequestBody CallingListDetailRequest request) {
        log.debug("createCallingList 요청: {}", request);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 생성", outboundService.createCallingList(request));
        log.debug("createCallingList 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists/update")
    @Operation(summary = "콜링리스트 수정", description = "콜링리스트 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "callingListUpdated",
                    value = "{\"success\":true,\"message\":\"콜링리스트 수정\",\"data\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"timestamp\":\"2026-02-02T14:26:36.8547878+09:00\"}"
                ))
        )
    })
    public ApiResponse<CallingListDetailSummary> updateCallingList(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CallingListUpdateCommand.class),
                examples = @ExampleObject(
                    name = "callingListDetail",
                    value = "{\"callingListDbid\":116,\"payload\":{\"tenantDbid\":1,\"name\":\"CallingList_20260305_002\",\"description\":\"콜링리스트 설명\",\"filterDbid\":114,\"logTableAccessDbid\":0,\"maxAttempts\":10,\"scriptDbid\":0,\"tableAccessDbid\":102,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}}"
                )
            )
        )
        @Valid @RequestBody CallingListUpdateCommand command) {
        log.debug("updateCallingList 요청: callingListDbid={}, request={}", command.callingListDbid(), command.payload());
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 수정",
            outboundService.updateCallingList(command.callingListDbid(), command.payload()));
        log.debug("updateCallingList 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists/delete")
    @Operation(summary = "콜링리스트 삭제", description = "콜링리스트를 삭제합니다.")
    public ApiResponse<Void> deleteCallingList(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "callingListDelete", value = "{\"dbid\":5001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int callingListDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        outboundService.deleteCallingList(callingListDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("콜링리스트 삭제", null);
        log.debug("deleteCallingList 응답: {}", response);
        return response;
    }

    @PostMapping("/table-access")
    @Operation(summary = "TableAccess 목록", description = "TableAccess 목록을 조회합니다.")
    public ApiResponse<List<TableAccessSummary>> listTableAccess(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "TableAccess 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "tableAccessList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listTableAccess 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TableAccessSummary>> response = ApiResponse.ok("TableAccess 목록",
            outboundService.listTableAccess(tenantDbid));
        log.debug("listTableAccess 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/table-access/get")
    @Operation(summary = "TableAccess 조회", description = "TableAccess를 DBID로 조회합니다.")
    public ApiResponse<TableAccessSummary> getTableAccess(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "TableAccess 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "tableAccessGet", value = "{\"dbid\":13001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int tableAccessDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTableAccess 요청: tableAccessDbid={}, tenantDbid={}", tableAccessDbid, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccess(tableAccessDbid, tenantDbid));
        log.debug("getTableAccess 응답: {}", response);
        return response;
    }

    @PostMapping("/table-access/by-name")
    @Operation(summary = "TableAccess 조회(이름)", description = "TableAccess를 이름으로 조회합니다.")
    public ApiResponse<TableAccessSummary> getTableAccessByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "TableAccess 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "tableAccessByName", value = "{\"name\":\"TABLE_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTableAccessByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccessByName(name, tenantDbid));
        log.debug("getTableAccessByName 응답: {}", response);
        return response;
    }

    @PostMapping("/treatment")
    @Operation(summary = "treatment 목록", description = "treatment 목록을 조회합니다.")
    public ApiResponse<List<TreatmentSummary>> listtreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "treatmentList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("treatment 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TreatmentSummary>> response = ApiResponse.ok("treatment 목록",
            outboundService.listTreatment(tenantDbid));
        log.debug("listtreatment 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/treatment/get")
    @Operation(summary = "Treatment 조회", description = "Treatment를 DBID로 조회합니다.")
    public ApiResponse<TreatmentSummary> getTreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "treatmentGet", value = "{\"dbid\":13001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int treatmentDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 조회",
            outboundService.getTreatment(treatmentDbid, tenantDbid));
        log.debug("getTreatment 응답: {}", response);
        return response;
    }

    @PostMapping("/treatment/by-name")
    @Operation(summary = "Treatment 조회(이름)", description = "Treatment를 이름으로 조회합니다.")
    public ApiResponse<TreatmentSummary> getTreatmentByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "treatmentByName", value = "{\"name\":\"TREATMENT_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTreatmentByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 조회",
            outboundService.getTreatmentByName(name, tenantDbid));
        log.debug("getTreatmentByName 응답: {}", response);
        return response;
    }

    @PostMapping("/treatment/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Treatment 생성", description = "Treatment를 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "treatmentCreated",
                    value = "{\"success\":true,\"message\":\"Treatment 생성\",\"data\":{\"dbid\":14001,\"name\":\"TREATMENT_A\",\"tenantDbid\":1,\"description\":\"Treatment 설명\",\"callResult\":\"Answer\",\"recActionCode\":\"CFGRACRetryIn\",\"attempts\":3,\"dateTime\":\"2026-03-20T10:30:00+09:00\",\"cycleAttempt\":5,\"interval\":10,\"increment\":5,\"callActionCode\":\"CFGCACTreatment\",\"destDnDbid\":15001,\"state\":\"CFGEnabled\",\"userProperties\":{\"default\":{\"key\":\"value\"}}},\"timestamp\":\"2026-03-20T10:30:00+09:00\"}"
                ))
        )
    })
    public ApiResponse<TreatmentSummary> createTreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = TreatmentRequest.class),
                examples = @ExampleObject(
                    name = "treatmentCreate",
                    value = "{\"tenantDbid\":1,\"name\":\"TREATMENT_A\",\"description\":\"Treatment 설명\",\"callResult\":\"Answer\",\"recActionCode\":\"CFGRACRetryIn\",\"attempts\":3,\"dateTime\":\"2026-03-20T10:30:00+09:00\",\"cycleAttempt\":5,\"interval\":10,\"increment\":5,\"callActionCode\":\"CFGCACTreatment\",\"destDnDbid\":15001,\"userProperties\":{\"default\":{\"key\":\"value\"}},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody TreatmentRequest request) {
        log.debug("createTreatment 요청: {}", request);
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 생성", outboundService.createTreatment(request));
        log.debug("createTreatment 응답: {}", response);
        return response;
    }

    @PostMapping("/treatment/update")
    @Operation(summary = "Treatment 수정", description = "Treatment 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "treatmentUpdated",
                    value = "{\"success\":true,\"message\":\"Treatment 수정\",\"data\":{\"dbid\":14001,\"name\":\"TREATMENT_A\",\"tenantDbid\":1,\"description\":\"Treatment 설명(수정)\",\"callResult\":\"Answer\",\"recActionCode\":\"CFGRACRetryIn\",\"attempts\":3,\"dateTime\":\"2026-03-20T10:30:00+09:00\",\"cycleAttempt\":5,\"interval\":10,\"increment\":5,\"callActionCode\":\"CFGCACTreatment\",\"destDnDbid\":15001,\"state\":\"CFGEnabled\",\"userProperties\":{\"default\":{\"key\":\"value\"}}},\"timestamp\":\"2026-03-20T10:30:00+09:00\"}"
                ))
        )
    })
    public ApiResponse<TreatmentSummary> updateTreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = TreatmentUpdateCommand.class),
                examples = @ExampleObject(
                    name = "treatmentUpdate",
                    value = "{\"treatmentDbid\":14001,\"payload\":{\"tenantDbid\":1,\"name\":\"TREATMENT_A\",\"description\":\"Treatment 설명(수정)\",\"callResult\":\"Answer\",\"recActionCode\":\"CFGRACRetryIn\",\"attempts\":3,\"dateTime\":\"2026-03-20T10:30:00+09:00\",\"cycleAttempt\":5,\"interval\":10,\"increment\":5,\"callActionCode\":\"CFGCACTreatment\",\"destDnDbid\":15001,\"userProperties\":{\"default\":{\"key\":\"value\"}},\"enabled\":true}}"
                )
            )
        )
        @Valid @RequestBody TreatmentUpdateCommand command) {
        log.debug("updateTreatment 요청: treatmentDbid={}, request={}", command.treatmentDbid(), command.payload());
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 수정",
            outboundService.updateTreatment(command.treatmentDbid(), command.payload()));
        log.debug("updateTreatment 응답: {}", response);
        return response;
    }

    @PostMapping("/treatment/delete")
    @Operation(summary = "Treatment 삭제", description = "Treatment를 삭제합니다.")
    public ApiResponse<Void> deleteTreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "treatmentDelete", value = "{\"dbid\":14001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int treatmentDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        outboundService.deleteTreatment(treatmentDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Treatment 삭제", null);
        log.debug("deleteTreatment 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns")
    @Operation(summary = "캠페인 목록", description = "캠페인 목록을 조회합니다.")
    public ApiResponse<List<CampaignSummary>> listCampaigns(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "campaignList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCampaigns 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignSummary>> response = ApiResponse.ok("캠페인 목록", outboundService.listCampaigns(tenantDbid));
        log.debug("listCampaigns 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/campaign-groups")
    @Operation(summary = "CampaignGroup 목록", description = "캠페인 그룹 목록을 조회합니다.")
    public ApiResponse<List<CampaignGroupSummary>> listCampaignGroups(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "campaignGroupList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCampaignGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignGroupSummary>> response = ApiResponse.ok("CampaignGroup 목록",
            outboundService.listCampaignGroups(tenantDbid));
        log.debug("listCampaignGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @PostMapping("/campaign-groups/get")
    @Operation(summary = "CampaignGroup 조회", description = "캠페인 그룹을 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupResponse",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> getCampaignGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "campaignGroupGet", value = "{\"dbid\":6001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int groupDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroup(groupDbid, tenantDbid));
        log.debug("getCampaignGroup 응답: {}", response);
        return response;
    }

    @PostMapping("/campaign-groups/by-name")
    @Operation(summary = "CampaignGroup 조회(이름)", description = "캠페인 그룹을 이름으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupByNameResponse",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> getCampaignGroupByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "campaignGroupByName", value = "{\"name\":\"GROUP_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroupByName(name, tenantDbid));
        log.debug("getCampaignGroupByName 응답: {}", response);
        return response;
    }

    @PostMapping("/campaign-groups/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "CampaignGroup 생성", description = "캠페인 그룹을 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupCreated",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> createCampaignGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignGroupRequest.class),
                examples = @ExampleObject(
                    name = "campaignGroupCreate",
                    value = "{\"tenantDbid\":1,\"campaignDbid\":115,\"groupDbid\":129,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign_20260305_001@아웃바운드2\",\"description\":\"desc\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":141,\"trunkGroupDnDbid\":137,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":101,\"serverDbids\":[107,108],\"userProperties\":{},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody CampaignGroupRequest request) {
        log.debug("createCampaignGroup 요청: {}", request);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 생성",
            outboundService.createCampaignGroup(request));
        log.debug("createCampaignGroup 응답: {}", response);
        return response;
    }

    @PostMapping("/campaign-groups/update")
    @Operation(summary = "CampaignGroup 수정", description = "캠페인 그룹 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupUpdated",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 수정\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> updateCampaignGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignGroupUpdateCommand.class),
                examples = @ExampleObject(
                    name = "campaignGroupUpdate",
                    value = "{\"groupDbid\":108,\"payload\":{\"tenantDbid\":1,\"campaignDbid\":115,\"groupDbid\":129,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign_20260305_001@아웃바운드2\",\"description\":null,\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":141,\"trunkGroupDnDbid\":137,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":101,\"serverDbids\":[107,108],\"userProperties\":{},\"enabled\":true}}"
                )
            )
        )
        @Valid @RequestBody CampaignGroupUpdateCommand command) {
        log.debug("updateCampaignGroup 요청: groupDbid={}, request={}", command.groupDbid(), command.payload());
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 수정",
            outboundService.updateCampaignGroup(command.groupDbid(), command.payload()));
        log.debug("updateCampaignGroup 응답: {}", response);
        return response;
    }

    @PostMapping("/campaign-groups/delete")
    @Operation(summary = "CampaignGroup 삭제", description = "캠페인 그룹을 삭제합니다.")
    public ApiResponse<Void> deleteCampaignGroup(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "campaignGroupDelete", value = "{\"dbid\":6001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int groupDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        outboundService.deleteCampaignGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("CampaignGroup 삭제", null);
        log.debug("deleteCampaignGroup 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/get")
    @Operation(summary = "캠페인 조회", description = "캠페인을 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignResponse",
                    value = "{\"success\":true,\"message\":\"캠페인 조회\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":1,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignSummary> getCampaign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 조회 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "campaignGet", value = "{\"dbid\":7001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int campaignDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회", outboundService.getCampaign(campaignDbid, tenantDbid));
        log.debug("getCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/by-name")
    @Operation(summary = "캠페인 조회(이름)", description = "캠페인을 이름으로 조회합니다.")
    public ApiResponse<CampaignSummary> getCampaignByName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 조회(이름) 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = NameTenantRequest.class),
                examples = @ExampleObject(name = "campaignByName", value = "{\"name\":\"CMP_A\",\"tenantDbid\":1}")
            )
        )
        @RequestBody NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회", outboundService.getCampaignByName(name, tenantDbid));
        log.debug("getCampaignByName 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "캠페인 생성", description = "캠페인을 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignCreated",
                    value = "{\"success\":true,\"message\":\"캠페인 조회\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":1,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignSummary> createCampaign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignRequest.class),
                examples = @ExampleObject(
                    name = "campaignCreate",
                    value = "{\"tenantDbid\":1,\"name\":\"Campaign_20260305_003\",\"description\":\"캠페인설명\",\"scriptDbid\":0,\"callingListNames\":[\"CallingList_20260305_002\"],\"userProperties\":{},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody CampaignRequest request) {
        log.debug("createCampaign 요청: {}", request);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 생성", outboundService.createCampaign(request));
        log.debug("createCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/batch-create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "아웃바운드 배치 생성", description = "Filter/CallingList/Campaign/CampaignGroup을 순서대로 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "batchCreateResponse",
                    value = "{\"success\":true,\"message\":\"아웃바운드 배치 생성\",\"data\":{\"filter\":{\"dbid\":null,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"callingList\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":null,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"campaign\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":1,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"campaignGroup\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":null,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<Object> createOutboundBatch(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "아웃바운드 배치 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = OutboundBatchCreateCommand.class),
                examples = @ExampleObject(
                    name = "outboundBatchCreate",
                    value = "{\"detail\":true,\"request\":{\"filter\":{\"tenantDbid\":1,\"name\":\"Filter-CallingList_20260305_001\",\"description\":\"콜링리스트용 필터\",\"formatDbid\":103,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true},\"callingList\":{\"tenantDbid\":1,\"name\":\"CallingList_20260305_001\",\"description\":\"콜링리스트 설명\",\"filterDbid\":0,\"logTableAccessDbid\":0,\"maxAttempts\":10,\"scriptDbid\":0,\"tableAccessDbid\":102,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"campaign\":{\"tenantDbid\":1,\"name\":\"Campaign_20260305_001\",\"description\":\"Campaign설명\",\"scriptDbid\":0,\"callingListNames\":[\"CallingList_20260305_001\"],\"userProperties\":{},\"enabled\":true},\"campaignGroup\":{\"tenantDbid\":1,\"campaignDbid\":0,\"groupDbid\":127,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign_20260305_001@아웃바운드\",\"description\":\"캠페인그룹설명\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":141,\"trunkGroupDnDbid\":137,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":101,\"serverDbids\":[107,108],\"userProperties\":{},\"enabled\":true}}}"
                )
            )
        )
        @Valid @RequestBody OutboundBatchCreateCommand command) {
        boolean detail = command.detail() == null || command.detail();
        OutboundBatchCreateRequest request = command.request();
        log.debug("createOutboundBatch 요청: request={}, detail={}", request, detail);
        OutboundBatchCreateResponse fullResponse = outboundService.createOutboundBatch(request);
        Object body = detail ? fullResponse : outboundService.summarizeBatch(fullResponse);
        ApiResponse<Object> response = ApiResponse.ok("아웃바운드 배치 생성", body);
        log.debug("createOutboundBatch 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/update")
    @Operation(summary = "캠페인 수정", description = "캠페인 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignUpdated",
                    value = "{\"success\":true,\"message\":\"캠페인 수정\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":1,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":1,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignSummary> updateCampaign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignUpdateCommand.class),
                examples = @ExampleObject(
                    name = "campaignUpdate",
                    value = "{\"campaignDbid\":116,\"payload\":{\"tenantDbid\":1,\"name\":\"Campaign_20260305_003\",\"description\":\"캠페인설명\",\"scriptDbid\":0,\"callingListNames\":[\"CallingList_20260305_002\"],\"userProperties\":{},\"enabled\":true}}"
                )
            )
        )
        @Valid @RequestBody CampaignUpdateCommand command) {
        log.debug("updateCampaign 요청: campaignDbid={}, request={}", command.campaignDbid(), command.payload());
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 수정",
            outboundService.updateCampaign(command.campaignDbid(), command.payload()));
        log.debug("updateCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/delete")
    @Operation(summary = "캠페인 삭제", description = "캠페인을 삭제합니다.")
    public ApiResponse<Void> deleteCampaign(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 삭제 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = DbidTenantRequest.class),
                examples = @ExampleObject(name = "campaignDelete", value = "{\"dbid\":7001,\"tenantDbid\":1}")
            )
        )
        @RequestBody DbidTenantRequest request) {
        int campaignDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        outboundService.deleteCampaign(campaignDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("캠페인 삭제", null);
        log.debug("deleteCampaign 응답: {}", response);
        return response;
    }
}
