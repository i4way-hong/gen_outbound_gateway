package com.genoutbound.gateway.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.service.GenesysConfigClient;
import com.genoutbound.gateway.genesys.outbound.service.OutboundClient;
import com.genoutbound.gateway.genesys.stat.service.StatServerClient;
import com.genoutbound.gateway.genesys.tserver.service.TServerClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class ApiStatusController {

    private final GenesysConfigClient genesysConfigClient;
    private final OutboundClient outboundClient;
    private final StatServerClient statServerClient;
    private final TServerClient tServerClient;

    public ApiStatusController(GenesysConfigClient genesysConfigClient, OutboundClient outboundClient,
                               StatServerClient statServerClient, TServerClient tServerClient) {
        this.genesysConfigClient = genesysConfigClient;
        this.outboundClient = outboundClient;
        this.statServerClient = statServerClient;
        this.tServerClient = tServerClient;
    }

    @Operation(summary = "상태 조회", description = "애플리케이션 및 Genesys 연동 상태를 확인합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "status-ok",
                    value = "{\"success\":true,\"message\":\"서비스 정상\",\"data\":{\"status\":\"ok\",\"time\":\"2026-02-04T15:51:44+09:00\",\"genesys\":{\"configServer\":{\"enabled\":true,\"connected\":true,\"state\":\"Opened\",\"endpoints\":[{\"role\":\"primary\",\"endpoint\":\"ConfigServer\",\"ip\":\"172.168.30.61\",\"port\":2020},{\"role\":\"backup\",\"endpoint\":\"ConfigServer\",\"ip\":\"172.168.30.62\",\"port\":2020}],\"connectionPool\":{\"mode\":\"singleton\",\"max\":1,\"active\":1,\"idle\":0},\"info\":{\"clientName\":\"gw-client\",\"addpEnabled\":true,\"charset\":\"UTF-8\"}},\"outboundServer\":{\"enabled\":true,\"connected\":true,\"state\":\"Opened\",\"endpoints\":[{\"role\":\"primary\",\"uri\":\"tcp://172.168.30.70:7000\"}],\"connectionPool\":{\"mode\":\"per-request\",\"max\":1,\"active\":1,\"idle\":0},\"info\":{\"clientName\":\"gw-client\",\"appName\":\"ocserver\"}},\"statServer\":{\"enabled\":true,\"connected\":true,\"state\":\"Opened\",\"endpoints\":[{\"role\":\"primary\",\"endpoint\":\"Stat_EP_P\",\"ip\":\"172.168.30.2\",\"port\":7002},{\"role\":\"backup\",\"endpoint\":\"Stat_EP_B\",\"ip\":\"172.168.30.2\",\"port\":7002}],\"connectionPool\":{\"mode\":\"per-request\",\"max\":1,\"active\":1,\"idle\":0},\"info\":{\"clientName\":\"stat-client\",\"charset\":\"MS949\",\"addpEnabled\":true}},\"tserver\":{\"enabled\":true,\"connected\":true,\"state\":\"Opened\",\"endpoints\":[{\"role\":\"primary\",\"endpoint\":\"TServer_EP\",\"ip\":\"172.168.30.2\",\"port\":7001}],\"connectionPool\":{\"mode\":\"per-request\",\"max\":1,\"active\":1,\"idle\":0},\"info\":{\"clientName\":\"tserver-client\",\"charset\":\"MS949\",\"addpEnabled\":true}}}},\"timestamp\":\"2026-02-04T15:51:44+09:00\"}")
            ))
    })
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> status() {
        Map<String, Object> payload = new LinkedHashMap<>();
        Map<String, Object> genesys = new LinkedHashMap<>();
        genesys.put("configServer", genesysConfigClient.getConnectionStatus());
        genesys.put("outboundServer", outboundClient.getConnectionStatus());
    genesys.put("statServer", statServerClient.getConnectionStatus());
    genesys.put("tserver", tServerClient.getConnectionStatus());
        payload.put("status", "ok");
        payload.put("time", OffsetDateTime.now());
        payload.put("genesys", genesys);
        return ApiResponse.ok("서비스 정상", payload);
    }
}
