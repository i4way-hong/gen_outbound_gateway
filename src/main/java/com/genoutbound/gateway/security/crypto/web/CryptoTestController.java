package com.genoutbound.gateway.security.crypto.web;

import com.genoutbound.gateway.config.EncryptionProperties;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.security.crypto.Aes256;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crypto")
@Tag(name = "Crypto Test", description = "AES 암호화/복호화 테스트 API")
public class CryptoTestController {

    private final EncryptionProperties properties;
    private final ObjectMapper objectMapper;

    public CryptoTestController(EncryptionProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "암호화 테스트", description = "평문을 AES256으로 암호화하여 encData를 반환합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "encrypt-request",
                value = "{\"plainText\":\"hello-ccc\"}"),
            @ExampleObject(name = "encrypt-request-generic",
                value = "{\"campaignDbid\":104,\"groupDbid\":129}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "encrypt-response",
                        value = "{\"success\":true,\"message\":\"암호화 성공\",\"data\":{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"},\"timestamp\":\"2026-02-09T16:20:00+09:00\"}")
                }))
    })
    @PostMapping("/encrypt")
    public ApiResponse<EncryptResponse> encrypt(@RequestBody Map<String, Object> request) {
        validateEncryptionConfig();
        if (request == null || request.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "요청 본문이 비어 있습니다.");
        }
        Object plainText = request.get("plainText");
        String rawText;
        try {
            if (plainText instanceof String text && !text.isBlank()) {
                rawText = text;
            } else {
                rawText = objectMapper.writeValueAsString(request);
            }
            String encrypted = Aes256.encrypt(rawText, properties.getKey(), properties.getIv());
            return ApiResponse.ok("암호화 성공", new EncryptResponse(encrypted));
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "암호화할 요청 본문이 올바르지 않습니다.");
        }
    }

    @Operation(summary = "복호화 테스트", description = "encData를 AES256으로 복호화하여 평문을 반환합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
        examples = {
            @ExampleObject(name = "decrypt-request",
                value = "{\"encData\":\"BASE64_AES_ENCRYPTED_PAYLOAD\"}")
        }))
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "decrypt-response",
                        value = "{\"success\":true,\"message\":\"복호화 성공\",\"data\":{\"plainText\":\"hello-ccc\"},\"timestamp\":\"2026-02-09T16:20:00+09:00\"}")
                }))
    })
    @PostMapping("/decrypt")
    public ApiResponse<DecryptResponse> decrypt(@Valid @RequestBody DecryptRequest request) {
        validateEncryptionConfig();
        String decrypted = Aes256.decrypt(request.encData(), properties.getKey(), properties.getIv());
        return ApiResponse.ok("복호화 성공", new DecryptResponse(decrypted));
    }

    private void validateEncryptionConfig() {
        if (!properties.isEnabled()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "암호화 설정이 비활성화되어 있습니다.");
        }
        if (properties.getKey() == null || properties.getKey().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "암호화 키가 설정되지 않았습니다.");
        }
        if (properties.getIv() == null || properties.getIv().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "암호화 IV가 설정되지 않았습니다.");
        }
    }

    public record DecryptRequest(@NotBlank String encData) {
    }

    public record EncryptResponse(String encData) {
    }

    public record DecryptResponse(String plainText) {
    }
}
