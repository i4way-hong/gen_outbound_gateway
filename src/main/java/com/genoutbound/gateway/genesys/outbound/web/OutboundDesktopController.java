package com.genoutbound.gateway.genesys.outbound.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopAddRecordAcknowledgeRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopAddRecordRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopDoNotCallAcknowledgeRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopDoNotCallRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopEventResponse;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopPayloadResponse;
import com.genoutbound.gateway.genesys.outbound.service.OutboundDesktopEventListener;
import com.genoutbound.gateway.genesys.outbound.service.OutboundDesktopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Outbound Desktop 프로토콜(POC) REST API 엔드포인트를 제공합니다.
 */
@Tag(name = "Outbound Desktop API", description = "Outbound Desktop 메시지 POC")
@SecurityRequirement(name = "bearerAuth")
//@RestController
@RequestMapping("/api/v1/outbound/desktop")
public class OutboundDesktopController {

    private static final Logger log = LoggerFactory.getLogger(OutboundDesktopController.class);
    private final OutboundDesktopService outboundDesktopService;
    private final OutboundDesktopEventListener outboundDesktopEventListener;

    /**
     * OutboundDesktopController 생성자.
     *
     * @param outboundDesktopService Outbound Desktop 서비스
     * @param outboundDesktopEventListener Outbound Desktop 이벤트 리스너
     */
    public OutboundDesktopController(OutboundDesktopService outboundDesktopService,
                                     OutboundDesktopEventListener outboundDesktopEventListener) {
        this.outboundDesktopService = outboundDesktopService;
        this.outboundDesktopEventListener = outboundDesktopEventListener;
    }

    @PostMapping("/add-record")
    @Operation(summary = "AddRecord", description = "AddRecord 메시지를 KV 페이로드로 마샬링합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> addRecord(
            @Parameter(description = "AddRecord 요청")
            @Valid @RequestBody OutboundDesktopAddRecordRequest request) {
        log.debug("OutboundDesktop addRecord 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("AddRecord payload",
            outboundDesktopService.buildAddRecord(request));
        log.debug("OutboundDesktop addRecord 응답: {}", response);
        return response;
    }

    @PostMapping("/add-record/send")
    @Operation(summary = "AddRecord 전송", description = "AddRecord 메시지를 T-Server로 전송합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> sendAddRecord(
            @Parameter(description = "AddRecord 전송 요청")
            @Valid @RequestBody OutboundDesktopAddRecordRequest request) {
        log.debug("OutboundDesktop sendAddRecord 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("AddRecord send",
            outboundDesktopService.sendAddRecord(request));
        log.debug("OutboundDesktop sendAddRecord 응답: {}", response);
        return response;
    }

    @PostMapping("/add-record/ack")
    @Operation(summary = "AddRecordAcknowledge", description = "AddRecordAcknowledge 메시지를 KV 페이로드로 마샬링합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> addRecordAcknowledge(
            @Parameter(description = "AddRecordAcknowledge 요청")
            @Valid @RequestBody OutboundDesktopAddRecordAcknowledgeRequest request) {
        log.debug("OutboundDesktop addRecordAcknowledge 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("AddRecordAcknowledge payload",
            outboundDesktopService.buildAddRecordAcknowledge(request));
        log.debug("OutboundDesktop addRecordAcknowledge 응답: {}", response);
        return response;
    }

    @PostMapping("/add-record/ack/send")
    @Operation(summary = "AddRecordAcknowledge 전송", description = "AddRecordAcknowledge 메시지를 T-Server로 전송합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> sendAddRecordAcknowledge(
            @Parameter(description = "AddRecordAcknowledge 전송 요청")
            @Valid @RequestBody OutboundDesktopAddRecordAcknowledgeRequest request) {
        log.debug("OutboundDesktop sendAddRecordAcknowledge 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("AddRecordAcknowledge send",
            outboundDesktopService.sendAddRecordAcknowledge(request));
        log.debug("OutboundDesktop sendAddRecordAcknowledge 응답: {}", response);
        return response;
    }

    @PostMapping("/do-not-call")
    @Operation(summary = "DoNotCall", description = "DoNotCall 메시지를 KV 페이로드로 마샬링합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> doNotCall(
            @Parameter(description = "DoNotCall 요청")
            @Valid @RequestBody OutboundDesktopDoNotCallRequest request) {
        log.debug("OutboundDesktop doNotCall 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("DoNotCall payload",
            outboundDesktopService.buildDoNotCall(request));
        log.debug("OutboundDesktop doNotCall 응답: {}", response);
        return response;
    }

    @PostMapping("/do-not-call/send")
    @Operation(summary = "DoNotCall 전송", description = "DoNotCall 메시지를 T-Server로 전송합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> sendDoNotCall(
            @Parameter(description = "DoNotCall 전송 요청")
            @Valid @RequestBody OutboundDesktopDoNotCallRequest request) {
        log.debug("OutboundDesktop sendDoNotCall 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("DoNotCall send",
            outboundDesktopService.sendDoNotCall(request));
        log.debug("OutboundDesktop sendDoNotCall 응답: {}", response);
        return response;
    }

    @PostMapping("/do-not-call/ack")
    @Operation(summary = "DoNotCallAcknowledge", description = "DoNotCallAcknowledge 메시지를 KV 페이로드로 마샬링합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> doNotCallAcknowledge(
            @Parameter(description = "DoNotCallAcknowledge 요청")
            @Valid @RequestBody OutboundDesktopDoNotCallAcknowledgeRequest request) {
        log.debug("OutboundDesktop doNotCallAcknowledge 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("DoNotCallAcknowledge payload",
            outboundDesktopService.buildDoNotCallAcknowledge(request));
        log.debug("OutboundDesktop doNotCallAcknowledge 응답: {}", response);
        return response;
    }

    @PostMapping("/do-not-call/ack/send")
    @Operation(summary = "DoNotCallAcknowledge 전송", description = "DoNotCallAcknowledge 메시지를 T-Server로 전송합니다(POC).")
    public ApiResponse<OutboundDesktopPayloadResponse> sendDoNotCallAcknowledge(
            @Parameter(description = "DoNotCallAcknowledge 전송 요청")
            @Valid @RequestBody OutboundDesktopDoNotCallAcknowledgeRequest request) {
        log.debug("OutboundDesktop sendDoNotCallAcknowledge 요청: {}", request);
        ApiResponse<OutboundDesktopPayloadResponse> response = ApiResponse.ok("DoNotCallAcknowledge send",
            outboundDesktopService.sendDoNotCallAcknowledge(request));
        log.debug("OutboundDesktop sendDoNotCallAcknowledge 응답: {}", response);
        return response;
    }

    @GetMapping("/events")
    @Operation(summary = "수신 이벤트 조회", description = "T-Server EventUserEvent로 수신된 Outbound Desktop 이벤트를 조회합니다.")
    public ApiResponse<List<OutboundDesktopEventResponse>> getEvents(
            @Parameter(description = "메시지 타입 필터(예: AddRecord)")
            @RequestParam(required = false) String messageType,
            @Parameter(description = "UserEvent ID 필터")
            @RequestParam(required = false) Integer userEventId,
            @Parameter(description = "최근 N건만 반환")
            @RequestParam(required = false) Integer limit) {
        log.debug("OutboundDesktop events 조회 요청: messageType={}, userEventId={}, limit={}",
            messageType, userEventId, limit);
        List<OutboundDesktopEventResponse> events = outboundDesktopEventListener.getRecentEvents();
        if (messageType != null && !messageType.isBlank()) {
            events = events.stream()
                .filter(event -> event.messageType() != null
                    && event.messageType().equalsIgnoreCase(messageType))
                .toList();
        }
        if (userEventId != null) {
            events = events.stream()
                .filter(event -> event.userEventId() != null
                    && event.userEventId().equals(userEventId))
                .toList();
        }
        if (limit != null && limit > 0 && events.size() > limit) {
            events = events.subList(events.size() - limit, events.size());
        }
        ApiResponse<List<OutboundDesktopEventResponse>> response = ApiResponse.ok("OutboundDesktop events", events);
        log.debug("OutboundDesktop events 응답: {}", response);
        return response;
    }

    @PostMapping("/events/clear")
    @Operation(summary = "수신 이벤트 버퍼 초기화", description = "수신된 Outbound Desktop 이벤트 버퍼를 초기화합니다.")
    public ApiResponse<Void> clearEvents() {
        log.debug("OutboundDesktop events clear 요청");
        outboundDesktopEventListener.clear();
        ApiResponse<Void> response = ApiResponse.ok("OutboundDesktop events cleared", null);
        log.debug("OutboundDesktop events clear 응답: {}", response);
        return response;
    }
}
