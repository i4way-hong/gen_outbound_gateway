package com.genoutbound.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genoutbound.gateway.security.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${local.server.port}")
    private int port;

    @Test
    void loginIssuesTokenAndAllowsAccess() throws Exception {
        AuthRequest request = new AuthRequest("test", "test");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });
    String baseUrl = "http://localhost:" + port;
    ResponseEntity<String> loginResponse = restTemplate.postForEntity(baseUrl + "/auth/login", request, String.class);
    assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    String responseBody = loginResponse.getBody();

        JsonNode json = objectMapper.readTree(responseBody);
        String accessToken = json.path("data").path("accessToken").asText();
        assertThat(accessToken).isNotBlank();

    ResponseEntity<String> unauthorizedResponse = restTemplate.getForEntity(baseUrl + "/api/status", String.class);
    assertThat(unauthorizedResponse.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        ResponseEntity<String> authorizedResponse = restTemplate.exchange(
            baseUrl + "/api/status",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );
        assertThat(authorizedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
