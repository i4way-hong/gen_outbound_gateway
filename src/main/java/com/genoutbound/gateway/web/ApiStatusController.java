package com.genoutbound.gateway.web;

import com.genoutbound.gateway.core.ApiResponse;
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

    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> status() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "ok");
        payload.put("time", OffsetDateTime.now());
        return ApiResponse.ok("서비스 정상", payload);
    }
}
