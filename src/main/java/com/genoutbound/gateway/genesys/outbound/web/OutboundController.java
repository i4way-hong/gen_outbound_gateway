package com.genoutbound.gateway.genesys.outbound.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundCommandRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDialRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundStatusResponse;
import com.genoutbound.gateway.genesys.outbound.service.OutboundService;
import com.genoutbound.gateway.web.annotation.CccEncryptedController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Outbound 관련 REST API 엔드포인트를 제공합니다.
 */
@Tag(name = "Outbound API", description = "Genesys Outbound 제어 API")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "성공",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "success",
                value = "{\"success\":true,\"message\":\"요청 성공\",\"data\":{},\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "잘못된 요청",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "bad-request",
                value = "{\"success\":false,\"message\":\"요청 값이 올바르지 않습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "502",
        description = "Outbound 요청 실패",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = @ExampleObject(name = "outbound-bad-gateway",
                value = "{\"success\":false,\"message\":\"Outbound 서버 요청 실패\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"))
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
        description = "Outbound 연동 실패",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "outbound-unavailable",
                    value = "{\"success\":false,\"message\":\"Outbound 서버 연결 상태가 올바르지 않습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "outbound-disabled",
                    value = "{\"success\":false,\"message\":\"Outbound 서버 연동이 비활성화되어 있습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "outbound-request-failed",
                    value = "{\"success\":false,\"message\":\"Outbound 요청 처리 실패\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "outbound-connection-failed",
                    value = "{\"success\":false,\"message\":\"Outbound 서버 연결 실패\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    )
})
@CccEncryptedController
@RestController
@RequestMapping("/api/v1/outbound")
public class OutboundController {

    private static final Logger log = LoggerFactory.getLogger(OutboundController.class);
    private final OutboundService outboundService;

    /**
     * OutboundController 생성자.
     *
     * @param outboundService Outbound 서비스
     */
    public OutboundController(OutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @PostMapping("/campaigns/load")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "캠페인 로드", description = "Outbound 캠페인을 로드합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "campaign-command",
            value = "{\"campaignDbid\":104,\"groupDbid\":129}")))
    /**
     * 캠페인을 로드합니다.
     */
    public ApiResponse<OutboundStatusResponse> loadCampaign(
            @Parameter(description = "캠페인 제어 요청")
            @Valid @RequestBody OutboundCommandRequest request) {
        log.debug("Outbound loadCampaign 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Campaign load", outboundService.loadCampaign(request));
        log.debug("Outbound loadCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/unload")
    @Operation(summary = "캠페인 언로드", description = "Outbound 캠페인을 언로드합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "campaign-command",
            value = "{\"campaignDbid\":104,\"groupDbid\":129}")))
    /**
     * 캠페인을 언로드합니다.
     */
    public ApiResponse<OutboundStatusResponse> unloadCampaign(
            @Parameter(description = "캠페인 제어 요청")
            @Valid @RequestBody OutboundCommandRequest request) {
        log.debug("Outbound unloadCampaign 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Campaign unload", outboundService.unloadCampaign(request));
        log.debug("Outbound unloadCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/force-unload")
    @Operation(summary = "캠페인 강제 언로드", description = "Outbound 캠페인을 강제 언로드합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "campaign-command",
            value = "{\"campaignDbid\":104,\"groupDbid\":129}")))
    /**
     * 캠페인을 강제로 언로드합니다.
     */
    public ApiResponse<OutboundStatusResponse> forceUnloadCampaign(
            @Parameter(description = "캠페인 제어 요청")
            @Valid @RequestBody OutboundCommandRequest request) {
        log.debug("Outbound forceUnloadCampaign 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Campaign force unload", outboundService.forceUnloadCampaign(request));
        log.debug("Outbound forceUnloadCampaign 응답: {}", response);
        return response;
    }

    @PostMapping("/dial/start")
    @Operation(summary = "다이얼 시작", description = "Outbound 다이얼링을 시작합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "dial-request",
            value = "{\"campaignDbid\":104,\"groupDbid\":129,\"dialMode\":\"predict\",\"optimizeMethod\":\"busyfactor\",\"optimizeGoal\":80}")))
    /**
     * 다이얼링을 시작합니다.
     */
    public ApiResponse<OutboundStatusResponse> startDialing(
            @Parameter(description = "다이얼링 요청")
            @Valid @RequestBody OutboundDialRequest request) {
        log.debug("Outbound startDialing 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Dialing start", outboundService.startDialing(request));
        log.debug("Outbound startDialing 응답: {}", response);
        return response;
    }

    @PostMapping("/dial/stop")
    @Operation(summary = "다이얼 중지", description = "Outbound 다이얼링을 중지합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "campaign-command",
            value = "{\"campaignDbid\":104,\"groupDbid\":129}")))
    /**
     * 다이얼링을 중지합니다.
     */
    public ApiResponse<OutboundStatusResponse> stopDialing(
            @Parameter(description = "다이얼 중지 요청")
            @Valid @RequestBody OutboundCommandRequest request) {
        log.debug("Outbound stopDialing 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Dialing stop", outboundService.stopDialing(request));
        log.debug("Outbound stopDialing 응답: {}", response);
        return response;
    }

    @PostMapping("/campaigns/status")
    @Operation(summary = "캠페인 상태 조회", description = "Outbound 캠페인 상태를 조회합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = @ExampleObject(name = "campaign-command",
            value = "{\"campaignDbid\":104,\"groupDbid\":129}")))
    /**
     * 캠페인 상태를 조회합니다.
     */
    public ApiResponse<OutboundStatusResponse> campaignStatus(
            @Parameter(description = "캠페인 상태 요청")
            @Valid @RequestBody OutboundCommandRequest request) {
        log.debug("Outbound campaignStatus 요청: {}", request);
        ApiResponse<OutboundStatusResponse> response = ApiResponse.ok("Campaign status", outboundService.getCampaignStatus(request));
        log.debug("Outbound campaignStatus 응답: {}", response);
        return response;
    }

    @GetMapping("/health")
    @Operation(summary = "헬스 체크", description = "Outbound API 상태를 확인합니다.")
    /**
     * Outbound API 헬스 체크.
     */
    public ApiResponse<String> health() {
        ApiResponse<String> response = ApiResponse.ok("Outbound API ready", "OK");
        log.debug("Outbound health 응답: {}", response);
        return response;
    }
}
