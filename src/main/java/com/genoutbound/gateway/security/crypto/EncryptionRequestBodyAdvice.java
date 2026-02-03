package com.genoutbound.gateway.security.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import com.genoutbound.gateway.config.EncryptionProperties;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.web.ConfigurationApiController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@ControllerAdvice
public class EncryptionRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final EncryptionProperties properties;

    public EncryptionRequestBodyAdvice(EncryptionProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, java.lang.reflect.Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if (!properties.isEnabled()) {
            return false;
        }
        return isConfigurationApiController(methodParameter);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           java.lang.reflect.Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        String rawBody = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
        if (rawBody == null || rawBody.isBlank()) {
            return inputMessage;
        }
        JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(rawBody);
        if (node != null && node.has("encData")) {
            String encData = node.path("encData").asText();
            if (encData == null || encData.isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "encData 값이 비어 있습니다.");
            }
            String decrypted = Aes256.decrypt(encData, properties.getKey(), properties.getIv());
            return new ResettableHttpInputMessage(inputMessage.getHeaders(), decrypted);
        }
        return new ResettableHttpInputMessage(inputMessage.getHeaders(), rawBody);
    }

    private boolean isConfigurationApiController(MethodParameter parameter) {
        return AnnotatedElementUtils.hasAnnotation(parameter.getContainingClass(),
            ConfigurationApiController.class);
    }

    private static class ResettableHttpInputMessage implements HttpInputMessage {
        private final HttpHeaders headers;
        private final byte[] bodyBytes;

        private ResettableHttpInputMessage(HttpHeaders headers, String body) {
            this.headers = headers;
            this.bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        @Override
        public java.io.InputStream getBody() {
            return new ByteArrayInputStream(bodyBytes);
        }
    }
}
