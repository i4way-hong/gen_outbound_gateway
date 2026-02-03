package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateResponse;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * 아웃바운드(콜링리스트/필터/캠페인 등) 관련 API를 제공합니다.
 */
@ConfigurationApiController
public class OutboundConfigController {

    private static final Logger log = LoggerFactory.getLogger(OutboundConfigController.class);
    private final OutboundConfigService outboundService;

    public OutboundConfigController(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    @GetMapping("/calling-lists")
    @Operation(summary = "콜링리스트 목록", description = "콜링리스트 목록을 조회합니다.")
    public ApiResponse<List<CallingListDetailSummary>> listCallingLists(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listCallingLists 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CallingListDetailSummary>> response = ApiResponse.ok("콜링리스트 목록",
            outboundService.listCallingLists(tenantDbid));
        log.debug("listCallingLists 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/filters")
    @Operation(summary = "Filter 목록", description = "Filter 목록을 조회합니다.")
    public ApiResponse<List<FilterSummary>> listFilters(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listFilters 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FilterSummary>> response = ApiResponse.ok("Filter 목록", outboundService.listFilters(tenantDbid));
        log.debug("listFilters 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/filters/{filterDbid}")
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
        @Parameter(description = "Filter DBID", example = "4001")
        @PathVariable int filterDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilter(filterDbid, tenantDbid));
        log.debug("getFilter 응답: {}", response);
        return response;
    }

    @GetMapping("/filters/by-name")
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
        @Parameter(description = "Filter 이름", example = "FILTER_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getFilterByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilterByName(name, tenantDbid));
        log.debug("getFilterByName 응답: {}", response);
        return response;
    }

    @GetMapping("/formats")
    @Operation(summary = "Format 목록", description = "Format 목록을 조회합니다.")
    public ApiResponse<List<FormatSummary>> listFormats(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listFormats 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FormatSummary>> response = ApiResponse.ok("Format 목록", outboundService.listFormats(tenantDbid));
        log.debug("listFormats 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/formats/{formatDbid}")
    @Operation(summary = "Format 조회", description = "Format을 DBID로 조회합니다.")
    public ApiResponse<FormatSummary> getFormat(
        @Parameter(description = "Format DBID", example = "12001")
        @PathVariable int formatDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getFormat 요청: formatDbid={}, tenantDbid={}", formatDbid, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회", outboundService.getFormat(formatDbid, tenantDbid));
        log.debug("getFormat 응답: {}", response);
        return response;
    }

    @GetMapping("/formats/by-name")
    @Operation(summary = "Format 조회(이름)", description = "Format을 이름으로 조회합니다.")
    public ApiResponse<FormatSummary> getFormatByName(
        @Parameter(description = "Format 이름", example = "FORMAT_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getFormatByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회", outboundService.getFormatByName(name, tenantDbid));
        log.debug("getFormatByName 응답: {}", response);
        return response;
    }

    @PostMapping("/filters")
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
                    value = "{\"tenantDbid\":101,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"formatDbid\":104,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody FilterRequest request) {
        log.debug("createFilter 요청: {}", request);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 생성", outboundService.createFilter(request));
        log.debug("createFilter 응답: {}", response);
        return response;
    }

    @PutMapping("/filters/{filterDbid}")
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
        @Parameter(description = "Filter DBID", example = "4001")
        @PathVariable int filterDbid,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Filter 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = FilterRequest.class),
                examples = @ExampleObject(
                    name = "filterUpdate",
                    value = "{\"tenantDbid\":101,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터(수정)\",\"formatDbid\":104,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody FilterRequest request) {
        log.debug("updateFilter 요청: filterDbid={}, request={}", filterDbid, request);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 수정", outboundService.updateFilter(filterDbid, request));
        log.debug("updateFilter 응답: {}", response);
        return response;
    }

    @DeleteMapping("/filters/{filterDbid}")
    @Operation(summary = "Filter 삭제", description = "Filter를 삭제합니다.")
    public ApiResponse<Void> deleteFilter(
        @Parameter(description = "Filter DBID", example = "4001")
        @PathVariable int filterDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        outboundService.deleteFilter(filterDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Filter 삭제", null);
        log.debug("deleteFilter 응답: {}", response);
        return response;
    }

    @GetMapping("/calling-lists/{callingListDbid}")
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
        @Parameter(description = "콜링리스트 DBID", example = "5001")
        @PathVariable int callingListDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingList(callingListDbid, tenantDbid));
        log.debug("getCallingList 응답: {}", response);
        return response;
    }

    @GetMapping("/calling-lists/by-name")
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
        @Parameter(description = "콜링리스트 이름", example = "LIST_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCallingListByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingListByName(name, tenantDbid));
        log.debug("getCallingListByName 응답: {}", response);
        return response;
    }

    @PostMapping("/calling-lists")
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
                    value = "{\"tenantDbid\":101,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}"
                )
            )
        )
        @Valid @RequestBody CallingListDetailRequest request) {
        log.debug("createCallingList 요청: {}", request);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 생성", outboundService.createCallingList(request));
        log.debug("createCallingList 응답: {}", response);
        return response;
    }

    @PutMapping("/calling-lists/{callingListDbid}")
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
        @Parameter(description = "콜링리스트 DBID", example = "5001")
        @PathVariable int callingListDbid,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "콜링리스트 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CallingListDetailRequest.class),
                examples = @ExampleObject(
                    name = "callingListDetail",
                    value = "{\"tenantDbid\":101,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}"
                )
            )
        )
        @Valid @RequestBody CallingListDetailRequest request) {
        log.debug("updateCallingList 요청: callingListDbid={}, request={}", callingListDbid, request);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 수정",
            outboundService.updateCallingList(callingListDbid, request));
        log.debug("updateCallingList 응답: {}", response);
        return response;
    }

    @DeleteMapping("/calling-lists/{callingListDbid}")
    @Operation(summary = "콜링리스트 삭제", description = "콜링리스트를 삭제합니다.")
    public ApiResponse<Void> deleteCallingList(
        @Parameter(description = "콜링리스트 DBID", example = "5001")
        @PathVariable int callingListDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        outboundService.deleteCallingList(callingListDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("콜링리스트 삭제", null);
        log.debug("deleteCallingList 응답: {}", response);
        return response;
    }

    @GetMapping("/table-access")
    @Operation(summary = "TableAccess 목록", description = "TableAccess 목록을 조회합니다.")
    public ApiResponse<List<TableAccessSummary>> listTableAccess(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listTableAccess 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TableAccessSummary>> response = ApiResponse.ok("TableAccess 목록",
            outboundService.listTableAccess(tenantDbid));
        log.debug("listTableAccess 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/table-access/{tableAccessDbid}")
    @Operation(summary = "TableAccess 조회", description = "TableAccess를 DBID로 조회합니다.")
    public ApiResponse<TableAccessSummary> getTableAccess(
        @Parameter(description = "TableAccess DBID", example = "13001")
        @PathVariable int tableAccessDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getTableAccess 요청: tableAccessDbid={}, tenantDbid={}", tableAccessDbid, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccess(tableAccessDbid, tenantDbid));
        log.debug("getTableAccess 응답: {}", response);
        return response;
    }

    @GetMapping("/table-access/by-name")
    @Operation(summary = "TableAccess 조회(이름)", description = "TableAccess를 이름으로 조회합니다.")
    public ApiResponse<TableAccessSummary> getTableAccessByName(
        @Parameter(description = "TableAccess 이름", example = "TABLE_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getTableAccessByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccessByName(name, tenantDbid));
        log.debug("getTableAccessByName 응답: {}", response);
        return response;
    }

    @GetMapping("/campaigns")
    @Operation(summary = "캠페인 목록", description = "캠페인 목록을 조회합니다.")
    public ApiResponse<List<CampaignSummary>> listCampaigns(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listCampaigns 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignSummary>> response = ApiResponse.ok("캠페인 목록", outboundService.listCampaigns(tenantDbid));
        log.debug("listCampaigns 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/campaign-groups")
    @Operation(summary = "CampaignGroup 목록", description = "캠페인 그룹 목록을 조회합니다.")
    public ApiResponse<List<CampaignGroupSummary>> listCampaignGroups(
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("listCampaignGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignGroupSummary>> response = ApiResponse.ok("CampaignGroup 목록",
            outboundService.listCampaignGroups(tenantDbid));
        log.debug("listCampaignGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    @GetMapping("/campaign-groups/{groupDbid}")
    @Operation(summary = "CampaignGroup 조회", description = "캠페인 그룹을 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupResponse",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> getCampaignGroup(
        @Parameter(description = "CampaignGroup DBID", example = "6001")
        @PathVariable int groupDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroup(groupDbid, tenantDbid));
        log.debug("getCampaignGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/campaign-groups/by-name")
    @Operation(summary = "CampaignGroup 조회(이름)", description = "캠페인 그룹을 이름으로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupByNameResponse",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> getCampaignGroupByName(
        @Parameter(description = "CampaignGroup 이름", example = "GROUP_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCampaignGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroupByName(name, tenantDbid));
        log.debug("getCampaignGroupByName 응답: {}", response);
        return response;
    }

    @PostMapping("/campaign-groups")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "CampaignGroup 생성", description = "캠페인 그룹을 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupCreated",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 조회\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
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
                    value = "{\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign7@상담그룹2\",\"description\":null,\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"serverDbids\":[118,109],\"userProperties\":{},\"enabled\":true}"
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

    @PutMapping("/campaign-groups/{groupDbid}")
    @Operation(summary = "CampaignGroup 수정", description = "캠페인 그룹 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignGroupUpdated",
                    value = "{\"success\":true,\"message\":\"CampaignGroup 수정\",\"data\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignGroupSummary> updateCampaignGroup(
        @Parameter(description = "CampaignGroup DBID", example = "6001")
        @PathVariable int groupDbid,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CampaignGroup 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignGroupRequest.class),
                examples = @ExampleObject(
                    name = "campaignGroupUpdate",
                    value = "{\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign7@상담그룹2\",\"description\":null,\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"serverDbids\":[118,109],\"userProperties\":{},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody CampaignGroupRequest request) {
        log.debug("updateCampaignGroup 요청: groupDbid={}, request={}", groupDbid, request);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 수정",
            outboundService.updateCampaignGroup(groupDbid, request));
        log.debug("updateCampaignGroup 응답: {}", response);
        return response;
    }

    @DeleteMapping("/campaign-groups/{groupDbid}")
    @Operation(summary = "CampaignGroup 삭제", description = "캠페인 그룹을 삭제합니다.")
    public ApiResponse<Void> deleteCampaignGroup(
        @Parameter(description = "CampaignGroup DBID", example = "6001")
        @PathVariable int groupDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        outboundService.deleteCampaignGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("CampaignGroup 삭제", null);
        log.debug("deleteCampaignGroup 응답: {}", response);
        return response;
    }

    @GetMapping("/campaigns/{campaignDbid}")
    @Operation(summary = "캠페인 조회", description = "캠페인을 DBID로 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignResponse",
                    value = "{\"success\":true,\"message\":\"캠페인 조회\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":101,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignSummary> getCampaign(
        @Parameter(description = "캠페인 DBID", example = "7001")
        @PathVariable int campaignDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회", outboundService.getCampaign(campaignDbid, tenantDbid));
        log.debug("getCampaign 응답: {}", response);
        return response;
    }

    @GetMapping("/campaigns/by-name")
    @Operation(summary = "캠페인 조회(이름)", description = "캠페인을 이름으로 조회합니다.")
    public ApiResponse<CampaignSummary> getCampaignByName(
        @Parameter(description = "캠페인 이름", example = "CMP_A")
        @RequestParam String name,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("getCampaignByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회", outboundService.getCampaignByName(name, tenantDbid));
        log.debug("getCampaignByName 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "캠페인 생성", description = "캠페인을 생성합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignCreated",
                    value = "{\"success\":true,\"message\":\"캠페인 조회\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":101,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
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
                    value = "{\"tenantDbid\":101,\"name\":\"Campaign7\",\"description\":null,\"scriptDbid\":0,\"callingListNames\":[\"Calling List2\"],\"userProperties\":{},\"enabled\":true}"
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
                    value = "{\"success\":true,\"message\":\"아웃바운드 배치 생성\",\"data\":{\"filter\":{\"dbid\":null,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"enabled\":true,\"formatDbid\":104,\"formatName\":null,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}}},\"callingList\":{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":null,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"campaign\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":101,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"campaignGroup\":{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":null,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<Object> createOutboundBatch(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "아웃바운드 배치 생성 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = OutboundBatchCreateRequest.class),
                examples = @ExampleObject(
                    name = "outboundBatchCreate",
                    value = "{\"filter\":{\"tenantDbid\":101,\"name\":\"Filter-CallingList2\",\"description\":\"콜링리스트용 필터\",\"formatDbid\":104,\"userProperties\":{\"default\":{\"criteria\":\"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\":\"\"}},\"enabled\":true},\"callingList\":{\"tenantDbid\":101,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}},\"campaign\":{\"tenantDbid\":101,\"name\":\"Campaign7\",\"description\":null,\"scriptDbid\":0,\"callingListNames\":[\"Calling List2\"],\"userProperties\":{},\"enabled\":true},\"campaignGroup\":{\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"name\":\"Campaign7@상담그룹2\",\"description\":null,\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"serverDbids\":[118,109],\"userProperties\":{},\"enabled\":true}}"
                )
            )
        )
        @Valid @RequestBody OutboundBatchCreateRequest request,
        @Parameter(description = "상세 응답 여부", example = "true")
        @RequestParam(defaultValue = "true") boolean detail) {
        log.debug("createOutboundBatch 요청: request={}, detail={}", request, detail);
        OutboundBatchCreateResponse fullResponse = outboundService.createOutboundBatch(request);
        Object body = detail ? fullResponse : outboundService.summarizeBatch(fullResponse);
        ApiResponse<Object> response = ApiResponse.ok("아웃바운드 배치 생성", body);
        log.debug("createOutboundBatch 응답: {}", response);
        return response;
    }

    @PutMapping("/campaigns/{campaignDbid}")
    @Operation(summary = "캠페인 수정", description = "캠페인 정보를 수정합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "campaignUpdated",
                    value = "{\"success\":true,\"message\":\"캠페인 수정\",\"data\":{\"dbid\":127,\"name\":\"Campaign7\",\"description\":null,\"enabled\":true,\"tenantDbid\":101,\"scriptDbid\":0,\"state\":\"CFGEnabled\",\"callingLists\":[{\"dbid\":102,\"name\":\"Calling List2\",\"description\":\"콜링리스트 설명\",\"filterDbid\":106,\"logTableAccessDbid\":0,\"maxAttempts\":3,\"scriptDbid\":0,\"tableAccessDbid\":101,\"timeFrom\":28800,\"timeTo\":64800,\"enabled\":true,\"treatmentDbids\":[101],\"userProperties\":{\"OCServer\":{\"CPNDigits\":\"0234881010\"}}}],\"campaignGroups\":[{\"dbid\":124,\"name\":\"Campaign7@상담그룹2\",\"enabled\":true,\"tenantDbid\":101,\"campaignDbid\":127,\"groupDbid\":105,\"groupType\":\"CFGAgentGroup\",\"description\":null,\"state\":\"CFGEnabled\",\"dialMode\":\"CFGDMPredict\",\"operationMode\":\"CFGOMManual\",\"numOfChannels\":10,\"optMethod\":\"CFGOMBusyFactor\",\"optMethodValue\":80,\"minRecBuffSize\":4,\"optRecBuffSize\":6,\"origDnDbid\":0,\"trunkGroupDnDbid\":0,\"scriptDbid\":0,\"interactionQueueDbid\":0,\"ivrProfileDbid\":0,\"servers\":[{\"dbid\":118,\"name\":\"ocserver\"},{\"dbid\":109,\"name\":\"statserver\"}],\"origDnNumber\":null,\"trunkGroupDnNumber\":null,\"userProperties\":{}}]},\"timestamp\":\"2026-02-02T14:27:56.2282685+09:00\"}"
                ))
        )
    })
    public ApiResponse<CampaignSummary> updateCampaign(
        @Parameter(description = "캠페인 DBID", example = "7001")
        @PathVariable int campaignDbid,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "캠페인 수정 요청",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CampaignRequest.class),
                examples = @ExampleObject(
                    name = "campaignUpdate",
                    value = "{\"tenantDbid\":101,\"name\":\"Campaign7\",\"description\":null,\"scriptDbid\":0,\"callingListNames\":[\"Calling List2\"],\"userProperties\":{},\"enabled\":true}"
                )
            )
        )
        @Valid @RequestBody CampaignRequest request) {
        log.debug("updateCampaign 요청: campaignDbid={}, request={}", campaignDbid, request);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 수정", outboundService.updateCampaign(campaignDbid, request));
        log.debug("updateCampaign 응답: {}", response);
        return response;
    }

    @DeleteMapping("/campaigns/{campaignDbid}")
    @Operation(summary = "캠페인 삭제", description = "캠페인을 삭제합니다.")
    public ApiResponse<Void> deleteCampaign(
        @Parameter(description = "캠페인 DBID", example = "7001")
        @PathVariable int campaignDbid,
        @Parameter(description = "테넌트 DBID", example = "101")
        @RequestParam(required = false) Integer tenantDbid) {
        log.debug("deleteCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        outboundService.deleteCampaign(campaignDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("캠페인 삭제", null);
        log.debug("deleteCampaign 응답: {}", response);
        return response;
    }
}
