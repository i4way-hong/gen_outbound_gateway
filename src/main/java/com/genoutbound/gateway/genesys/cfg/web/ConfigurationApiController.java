package com.genoutbound.gateway.genesys.cfg.web;

import com.genoutbound.gateway.core.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping("/api/v1/configuration")
@Tag(name = "Configuration API", description = "Genesys Config 관련 설정 API")
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
            examples = {
                @ExampleObject(name = "bad-request",
                    value = "{\"success\":false,\"message\":\"요청 값이 올바르지 않습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "section-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 섹션입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "중복 데이터",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "person-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 상담사입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "agent-group-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 그룹입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "agent-login-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 AgentLogin입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "filter-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 Filter입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "calling-list-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 콜링리스트입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-group-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 CampaignGroup입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 캠페인입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "dngroup-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 DNGroup입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "dn-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 DN입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "transaction-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 트랜잭션입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "place-group-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 PlaceGroup입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "place-exists",
                    value = "{\"success\":false,\"message\":\"이미 존재하는 Place입니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "대상 없음",
        content = @Content(schema = @Schema(implementation = ApiResponse.class),
            examples = {
                @ExampleObject(name = "person-not-found",
                    value = "{\"success\":false,\"message\":\"상담사(을)를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "agent-group-not-found",
                    value = "{\"success\":false,\"message\":\"그룹을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "agent-login-not-found",
                    value = "{\"success\":false,\"message\":\"AgentLogin을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "filter-not-found",
                    value = "{\"success\":false,\"message\":\"Filter를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "calling-list-not-found",
                    value = "{\"success\":false,\"message\":\"콜링리스트를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-group-not-found",
                    value = "{\"success\":false,\"message\":\"CampaignGroup을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "campaign-not-found",
                    value = "{\"success\":false,\"message\":\"캠페인을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "dngroup-not-found",
                    value = "{\"success\":false,\"message\":\"DNGroup을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "dn-not-found",
                    value = "{\"success\":false,\"message\":\"DN을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "transaction-not-found",
                    value = "{\"success\":false,\"message\":\"트랜잭션을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "section-not-found",
                    value = "{\"success\":false,\"message\":\"섹션을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "place-group-not-found",
                    value = "{\"success\":false,\"message\":\"PlaceGroup을 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "place-not-found",
                    value = "{\"success\":false,\"message\":\"Place를 찾을 수 없습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
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
                    value = "{\"success\":false,\"message\":\"요청 처리 중 오류가 발생했습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "config-connect-failed",
                    value = "{\"success\":false,\"message\":\"Config Server 연결에 실패했습니다.\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}"),
                @ExampleObject(name = "config-connect-error",
                    value = "{\"success\":false,\"message\":\"Config Server 연결 실패\",\"data\":null,\"timestamp\":\"2026-01-30T10:00:00+09:00\"}")
            })
    )
})
public @interface ConfigurationApiController {
}
