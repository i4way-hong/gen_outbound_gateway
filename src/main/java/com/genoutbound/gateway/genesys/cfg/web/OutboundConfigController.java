package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundBatchCreateUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundCampaignUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundCampaignGroupUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundCallingListUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundFilterUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundFormatUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundTableAccessUseCaseHandler;
import com.genoutbound.gateway.genesys.cfg.web.support.OutboundTreatmentUseCaseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
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

    private final OutboundFilterUseCaseHandler filterHandler;
    private final OutboundCallingListUseCaseHandler callingListHandler;
    private final OutboundCampaignUseCaseHandler campaignHandler;
    private final OutboundCampaignGroupUseCaseHandler campaignGroupHandler;
    private final OutboundTreatmentUseCaseHandler treatmentHandler;
    private final OutboundTableAccessUseCaseHandler tableAccessHandler;
    private final OutboundFormatUseCaseHandler formatHandler;
    private final OutboundBatchCreateUseCaseHandler batchCreateHandler;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 핸들러 참조를 라우팅 처리 호출에만 사용하며 외부 mutable 참조를 재노출하지 않습니다.")
    public OutboundConfigController(OutboundFilterUseCaseHandler filterHandler,
                                    OutboundCallingListUseCaseHandler callingListHandler,
                                    OutboundCampaignUseCaseHandler campaignHandler,
                                    OutboundCampaignGroupUseCaseHandler campaignGroupHandler,
                                    OutboundTreatmentUseCaseHandler treatmentHandler,
                                    OutboundTableAccessUseCaseHandler tableAccessHandler,
                                    OutboundFormatUseCaseHandler formatHandler,
                                    OutboundBatchCreateUseCaseHandler batchCreateHandler) {
        this.filterHandler = filterHandler;
        this.callingListHandler = callingListHandler;
        this.campaignHandler = campaignHandler;
        this.campaignGroupHandler = campaignGroupHandler;
        this.treatmentHandler = treatmentHandler;
        this.tableAccessHandler = tableAccessHandler;
        this.formatHandler = formatHandler;
        this.batchCreateHandler = batchCreateHandler;
    }

    @PostMapping("/calling-lists")
    @Operation(summary = "콜링리스트 목록", description = "콜링리스트 목록을 조회합니다.")
    public ApiResponse<List<CallingListDetailSummary>> listCallingLists(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "callingList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        return callingListHandler.listCallingLists(request);
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
    return filterHandler.listFilters(request);
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
    return filterHandler.getFilter(request);
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
    return filterHandler.getFilterByName(request);
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
        return formatHandler.listFormats(request);
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
        return formatHandler.getFormat(request);
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
        return formatHandler.getFormatByName(request);
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
    return filterHandler.createFilter(request);
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
        return filterHandler.updateFilter(command);
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
    return filterHandler.deleteFilter(request);
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
        return callingListHandler.getCallingList(request);
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
        return callingListHandler.getCallingListByName(request);
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
        return callingListHandler.createCallingList(request);
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
        return callingListHandler.updateCallingList(command);
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
        return callingListHandler.deleteCallingList(request);
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
        return tableAccessHandler.listTableAccess(request);
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
        return tableAccessHandler.getTableAccess(request);
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
        return tableAccessHandler.getTableAccessByName(request);
    }

    @PostMapping("/treatment")
    @Operation(summary = "Treatment 목록", description = "Treatment 목록을 조회합니다.")
    public ApiResponse<List<TreatmentSummary>> listTreatment(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Treatment 조회 요청",
            required = false,
            content = @Content(
                schema = @Schema(implementation = TenantDbidRequest.class),
                examples = @ExampleObject(name = "treatmentList", value = "{\"tenantDbid\":1}")
            )
        )
        @RequestBody TenantDbidRequest request) {
        return treatmentHandler.listTreatment(request);
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
        return treatmentHandler.getTreatment(request);
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
        return treatmentHandler.getTreatmentByName(request);
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
        return treatmentHandler.createTreatment(request);
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
        return treatmentHandler.updateTreatment(command);
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
        return treatmentHandler.deleteTreatment(request);
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
        return campaignHandler.listCampaigns(request);
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
        return campaignGroupHandler.listCampaignGroups(request);
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
        return campaignGroupHandler.getCampaignGroup(request);
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
        return campaignGroupHandler.getCampaignGroupByName(request);
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
        return campaignGroupHandler.createCampaignGroup(request);
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
        return campaignGroupHandler.updateCampaignGroup(command);
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
        return campaignGroupHandler.deleteCampaignGroup(request);
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
        return campaignHandler.getCampaign(request);
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
        return campaignHandler.getCampaignByName(request);
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
        return campaignHandler.createCampaign(request);
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
        return batchCreateHandler.createOutboundBatch(command);
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
        return campaignHandler.updateCampaign(command);
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
        return campaignHandler.deleteCampaign(request);
    }

}
