package com.genoutbound.gateway.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.scs.service.ScsEventService;
import com.genoutbound.gateway.sse.AppStatusEvent;
import com.genoutbound.gateway.sse.AppStatusSseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/scs")
@SecurityRequirement(name = "bearerAuth")
public class ScsSseController {

    private final AppStatusSseService sseService;
    private final ScsEventService scsEventService;

    public ScsSseController(AppStatusSseService sseService, ScsEventService scsEventService) {
        this.sseService = sseService;
        this.scsEventService = scsEventService;
    }

    @Operation(summary = "SCS Application 상태 SSE 구독", description = "Application 상태 변경 이벤트를 SSE로 전달합니다.")
    @GetMapping(value = "/app-status/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAppStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String sessionId = session == null ? null : session.getId();
        String clientIp = AppStatusSseService.resolveClientIp(
            new ServletServerHttpRequest(request).getHeaders(), request.getRemoteAddr());
        scsEventService.refreshCurrentStatuses();
        return sseService.subscribe(clientIp, sessionId);
    }

    @Operation(summary = "SCS SSE 메트릭 조회", description = "SSE 접속 현황 및 disconnect 사유 통계를 반환합니다.")
    @GetMapping("/metrics")
    public ApiResponse<Map<String, Object>> metrics() {
        return ApiResponse.ok("SSE 메트릭", sseService.getMetrics());
    }

    @Operation(summary = "SCS Application 현재 상태 조회", description = "application DBID를 전달하면 현재 상태를 반환합니다.")
    @GetMapping("/app-status")
    public ApiResponse<AppStatusEvent> getCurrentStatus(@RequestParam("dbid") Integer dbid) {
        return ApiResponse.ok("Application 상태", scsEventService.getCurrentStatus(dbid));
    }
}
