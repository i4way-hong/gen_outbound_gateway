package com.genoutbound.gateway.security.crypto.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.web.annotation.CccEncryptedController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CccEncryptedController
@RequestMapping("/api/v1/crypto/secure")
@Tag(name = "Crypto Secure", description = "CCC 암복호화 파이프라인 테스트 API")
public class CryptoSecureEchoController {

    @Operation(summary = "암복호화 에코", description = "CCC 암복호화 어드바이스를 통해 요청/응답을 테스트합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "echo-request",
                value = "{\"message\":\"hello-ccc\",\"payload\":{\"tenantDbid\":1}}"),
            @ExampleObject(name = "echo-request-encrypted",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "echo-response",
                        value = "{\"success\":true,\"message\":\"에코 성공\",\"data\":{\"message\":\"hello-ccc\",\"payload\":{\"tenantDbid\":1}},\"timestamp\":\"2026-02-09T16:20:00+09:00\"}"),
                    @ExampleObject(name = "echo-response-encrypted",
                        value = "{\"encData\":\"BASE64_AES_ENCRYPTED_RESPONSE\"}")
                }))
    })
    @PostMapping("/echo")
    public ApiResponse<EchoResponse> echo(@Valid @RequestBody EchoRequest request) {
        EchoResponse response = new EchoResponse(request.message(), request.payload());
        return ApiResponse.ok("에코 성공", response);
    }

    public record EchoRequest(@NotBlank String message, Map<String, Object> payload) {
    }

    public record EchoResponse(String message, Map<String, Object> payload) {
    }
}
