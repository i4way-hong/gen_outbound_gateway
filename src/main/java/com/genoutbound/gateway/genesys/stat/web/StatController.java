package com.genoutbound.gateway.genesys.stat.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import com.genoutbound.gateway.genesys.stat.service.StatServerClient;
import com.genoutbound.gateway.web.annotation.CccEncryptedController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 스킬그룹 현황조회 컨트롤러.
 */
@CccEncryptedController
public class StatController {

    private static final Logger log = LoggerFactory.getLogger(StatController.class);
    private final StatServerClient statServerClient;

    public StatController(StatServerClient statServerClient) {
        this.statServerClient = statServerClient;
    }

    /**
     * 스킬그룹별 상담사 상세조회
     */
    @Operation(summary = "스킬그룹 상담사 상태 조회", description = "Stat Server에서 스킬그룹 상담사 상태를 조회합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "skill-group-stat-request",
                value = "{\"NAME\":\"SalesSkillGroup\"}"),
            @ExampleObject(name = "skill-group-stat-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "skill-group-stat",
                        value = "{\"success\":true,\"message\":\"스킬그룹 상담사 상태 조회\",\"data\":{\"USERS\":[{\"EMPLOYEE_ID\":\"10001\",\"STAT\":\"Ready\"},{\"EMPLOYEE_ID\":\"10002\",\"STAT\":\"NotReady\"}]},\"timestamp\":\"2026-02-04T16:27:05+09:00\"}"),
                    @ExampleObject(name = "skill-group-stat-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/api/v1/stat/getSkillGrpStat")
    public ApiResponse<Map<String, Object>> getSkillGrpStat(@RequestBody Map<String, Object> param) {
        Map<String, Object> payload = new LinkedHashMap<>();
        try {
            String groupName = getParam(param, "NAME", "");
            if (groupName.isBlank()) {
                return new ApiResponse<>(false, "NAME 값이 필요합니다.", null, OffsetDateTime.now());
            }
            List<Map<String, Object>> users = statServerClient.getSkillGroupAgentStatuses(groupName);
            payload.put("USERS", users);
            return ApiResponse.ok("스킬그룹 상담사 상태 조회", payload);
        } catch (GenesysUnavailableException ex) {
            log.warn("getSkillGrpStat 실패", ex);
            return new ApiResponse<>(false, ex.getMessage(), null, OffsetDateTime.now());
        } catch (Exception ex) {
            log.error("getSkillGrpStat 오류", ex);
            return new ApiResponse<>(false, "스킬그룹 상담사 상태 조회 실패", null, OffsetDateTime.now());
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
}
