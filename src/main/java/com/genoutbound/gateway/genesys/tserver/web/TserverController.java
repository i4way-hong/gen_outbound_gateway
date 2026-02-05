package com.genoutbound.gateway.genesys.tserver.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import com.genoutbound.gateway.genesys.tserver.service.TServerClient;
import com.genoutbound.gateway.web.annotation.CccEncryptedController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * T-Server 연동 컨트롤러.
 */
@CccEncryptedController
public class TserverController {

    private static final Logger log = LoggerFactory.getLogger(TserverController.class);
    private final TServerClient tserverClient;

    public TserverController(TServerClient tserverClient) {
        this.tserverClient = tserverClient;
    }

    /**
     * 상담사 원격 로그아웃 송신 IF
     */
    @Operation(summary = "상담사 로그아웃", description = "T-Server로 상담사 로그아웃을 전송합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "logout-request",
                value = "{\"INLINE_NUM\":\"1000\"}"),
            @ExampleObject(name = "logout-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "logout",
                        value = "{\"success\":true,\"message\":\"상담사 로그아웃\",\"data\":{\"RESULT\":\"T\",\"LOGIN_ID\":\"2000\",\"RESULT_MSG\":\"\"},\"timestamp\":\"2026-02-04T16:27:05+09:00\"}"),
                    @ExampleObject(name = "logout-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/api/v1/voice/logout")
    public ApiResponse<Map<String, Object>> logout(@RequestBody Map<String, Object> param) {
        return handleAgentAction(param, ActionType.LOGOUT);
    }

    /**
     * 상담사 원격 대기 송신 IF
     */
    @Operation(summary = "상담사 대기", description = "T-Server로 상담사 대기 상태 전환을 요청합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "ready-request",
                value = "{\"INLINE_NUM\":\"1000\"}"),
            @ExampleObject(name = "ready-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "ready",
                        value = "{\"success\":true,\"message\":\"상담사 대기\",\"data\":{\"RESULT\":\"T\",\"LOGIN_ID\":\"2000\",\"RESULT_MSG\":\"\"},\"timestamp\":\"2026-02-04T16:27:05+09:00\"}"),
                    @ExampleObject(name = "ready-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/api/v1/voice/ready")
    public ApiResponse<Map<String, Object>> ready(@RequestBody Map<String, Object> param) {
        return handleAgentAction(param, ActionType.READY);
    }

    /**
     * 상담사 원격 이석 송신 IF
     */
    @Operation(summary = "상담사 이석", description = "T-Server로 상담사 이석 상태 전환을 요청합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "not-ready-request",
                value = "{\"INLINE_NUM\":\"1000\",\"REASON_CODE\":\"10\"}"),
            @ExampleObject(name = "not-ready-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "not-ready",
                        value = "{\"success\":true,\"message\":\"상담사 이석\",\"data\":{\"RESULT\":\"T\",\"LOGIN_ID\":\"2000\",\"RESULT_MSG\":\"\"},\"timestamp\":\"2026-02-04T16:27:05+09:00\"}"),
                    @ExampleObject(name = "not-ready-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/api/v1/voice/notReady")
    public ApiResponse<Map<String, Object>> notReady(@RequestBody Map<String, Object> param) {
        return handleAgentAction(param, ActionType.NOT_READY);
    }

    /**
     * 상담사 상태확인 송신 IF
     */
    @Operation(summary = "상담사 상태 확인", description = "T-Server에서 상담사 상태를 확인합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "check-status-request",
                value = "{\"INLINE_NUM\":\"1000\"}"),
            @ExampleObject(name = "check-status-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "check-status",
                        value = "{\"success\":true,\"message\":\"상담사 상태 확인\",\"data\":{\"RESULT\":\"T\",\"LOGIN_ID\":\"2000\",\"RESULT_MSG\":\"\"},\"timestamp\":\"2026-02-04T16:27:05+09:00\"}"),
                    @ExampleObject(name = "check-status-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/api/v1/voice/checkStatus")
    public ApiResponse<Map<String, Object>> checkAgentStatus(@RequestBody Map<String, Object> param) {
        Map<String, Object> payload = new LinkedHashMap<>();
        try {
            String dn = getParam(param, "INLINE_NUM", "");
            if (dn.isBlank()) {
                return new ApiResponse<>(false, "INLINE_NUM 값이 필요합니다.", null, OffsetDateTime.now());
            }
            String loginId = tserverClient.executeWithProtocol(protocol -> {
                String agentId = tserverClient.register(protocol, dn);
                tserverClient.unregister(protocol, dn);
                return agentId;
            });
            payload.put("RESULT", "T");
            payload.put("LOGIN_ID", loginId);
            payload.put("RESULT_MSG", "");
            return ApiResponse.ok("상담사 상태 확인", payload);
        } catch (GenesysUnavailableException ex) {
            log.warn("checkAgentStatus 실패", ex);
            payload.put("RESULT", "F");
            payload.put("LOGIN_ID", "");
            payload.put("RESULT_MSG", ex.getMessage());
            return new ApiResponse<>(false, ex.getMessage(), payload, OffsetDateTime.now());
        } catch (Exception ex) {
            log.error("checkAgentStatus 오류", ex);
            payload.put("RESULT", "F");
            payload.put("LOGIN_ID", "");
            payload.put("RESULT_MSG", "상담사 상태 확인 실패");
            return new ApiResponse<>(false, "상담사 상태 확인 실패", payload, OffsetDateTime.now());
        }
    }

    private ApiResponse<Map<String, Object>> handleAgentAction(Map<String, Object> param, ActionType type) {
        Map<String, Object> payload = new LinkedHashMap<>();
        try {
            String dn = getParam(param, "INLINE_NUM", "");
            String reason = getParam(param, "REASON_CODE", "");
            if (dn.isBlank()) {
                return new ApiResponse<>(false, "INLINE_NUM 값이 필요합니다.", null, OffsetDateTime.now());
            }
            tserverClient.executeWithProtocol(protocol -> {
                tserverClient.register(protocol, dn);
                switch (type) {
                    case LOGOUT -> tserverClient.logout(protocol, dn);
                    case READY -> tserverClient.ready(protocol, dn);
                    case NOT_READY -> tserverClient.notReady(protocol, dn, reason);
                }
                tserverClient.unregister(protocol, dn);
                return null;
            });
            payload.put("RESULT", "T");
            payload.put("LOGIN_ID", "");
            payload.put("RESULT_MSG", "");
            return ApiResponse.ok(type.getMessage(), payload);
        } catch (GenesysUnavailableException ex) {
            log.warn("{} 실패", type, ex);
            payload.put("RESULT", "F");
            payload.put("LOGIN_ID", "");
            payload.put("RESULT_MSG", ex.getMessage());
            return new ApiResponse<>(false, ex.getMessage(), payload, OffsetDateTime.now());
        } catch (Exception ex) {
            log.error("{} 오류", type, ex);
            payload.put("RESULT", "F");
            payload.put("LOGIN_ID", "");
            payload.put("RESULT_MSG", type.getMessage() + " 실패");
            return new ApiResponse<>(false, type.getMessage() + " 실패", payload, OffsetDateTime.now());
        }
    }

    private String getParam(Map<String, Object> param, String key, String defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        Object value = param.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    private enum ActionType {
        LOGOUT("상담사 로그아웃"),
        READY("상담사 대기"),
        NOT_READY("상담사 이석");

        private final String message;

        ActionType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
