package com.genoutbound.gateway.security.crypto;

import com.genoutbound.gateway.config.EncryptionProperties;
import com.genoutbound.gateway.genesys.cfg.web.ConfigurationApiController;
import com.genoutbound.gateway.web.annotation.CccEncryptedController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class EncryptionResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final EncryptionProperties properties;
    private final ObjectMapper objectMapper;

    public EncryptionResponseBodyAdvice(EncryptionProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (!properties.isEnabled()) {
            return false;
        }
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(),
            ConfigurationApiController.class)
            || AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(),
            CccEncryptedController.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Map<?, ?> mapBody && mapBody.containsKey("encData")) {
            return body;
        }
        try {
            String json = objectMapper.writeValueAsString(body);
            String encrypted = Aes256.encrypt(json, properties.getKey(), properties.getIv());
            return Map.of("encData", encrypted);
        } catch (Exception ex) {
            throw new IllegalStateException("응답 암호화 실패", ex);
        }
    }
}
