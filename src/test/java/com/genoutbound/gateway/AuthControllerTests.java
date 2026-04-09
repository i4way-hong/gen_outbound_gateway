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

    ResponseEntity<String> unauthorizedResponse = restTemplate.getForEntity(baseUrl + "/api/v1/scs/metrics", String.class);
        assertThat(unauthorizedResponse.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        ResponseEntity<String> authorizedResponse = restTemplate.exchange(
            baseUrl + "/api/v1/scs/metrics",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );
        assertThat(authorizedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void refreshTokenIsOneTimeUse() throws Exception {
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

        JsonNode loginJson = objectMapper.readTree(loginResponse.getBody());
        String refreshToken = loginJson.path("data").path("refreshToken").asText();
        assertThat(refreshToken).isNotBlank();

        ResponseEntity<String> refreshResponse = restTemplate.postForEntity(
            baseUrl + "/auth/refresh",
            new com.genoutbound.gateway.security.dto.RefreshRequest(refreshToken),
            String.class
        );
        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> reusedResponse = restTemplate.postForEntity(
            baseUrl + "/auth/refresh",
            new com.genoutbound.gateway.security.dto.RefreshRequest(refreshToken),
            String.class
        );
        assertThat(reusedResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void logoutRevokesAccessToken() throws Exception {
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

        JsonNode loginJson = objectMapper.readTree(loginResponse.getBody());
        String accessToken = loginJson.path("data").path("accessToken").asText();
        String refreshToken = loginJson.path("data").path("refreshToken").asText();
        assertThat(accessToken).isNotBlank();

        ResponseEntity<String> logoutResponse = restTemplate.exchange(
            baseUrl + "/auth/logout",
            HttpMethod.POST,
            new HttpEntity<>(new com.genoutbound.gateway.security.dto.LogoutRequest(accessToken, refreshToken)),
            String.class
        );
        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> revokedResponse = restTemplate.exchange(
            baseUrl + "/api/v1/scs/metrics",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );
        assertThat(revokedResponse.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    void newLoginInvalidatesPreviousAccessToken() throws Exception {
        AuthRequest request = new AuthRequest("test", "test");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });

        String baseUrl = "http://localhost:" + port;
        ResponseEntity<String> firstLogin = restTemplate.postForEntity(baseUrl + "/auth/login", request, String.class);
        assertThat(firstLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode firstJson = objectMapper.readTree(firstLogin.getBody());
        String firstAccessToken = firstJson.path("data").path("accessToken").asText();
        assertThat(firstAccessToken).isNotBlank();

        ResponseEntity<String> secondLogin = restTemplate.postForEntity(baseUrl + "/auth/login", request, String.class);
        assertThat(secondLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(firstAccessToken);
        ResponseEntity<String> revokedResponse = restTemplate.exchange(
            baseUrl + "/api/v1/scs/metrics",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            String.class
        );
        assertThat(revokedResponse.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }

    @Test
    void logoutInvalidatesAllTokens() throws Exception {
        AuthRequest request = new AuthRequest("test", "test");

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });

        String baseUrl = "http://localhost:" + port;
        ResponseEntity<String> firstLogin = restTemplate.postForEntity(baseUrl + "/auth/login", request, String.class);
        assertThat(firstLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode firstJson = objectMapper.readTree(firstLogin.getBody());
        String firstAccessToken = firstJson.path("data").path("accessToken").asText();
        String refreshToken = firstJson.path("data").path("refreshToken").asText();

        ResponseEntity<String> secondLogin = restTemplate.postForEntity(baseUrl + "/auth/login", request, String.class);
        assertThat(secondLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode secondJson = objectMapper.readTree(secondLogin.getBody());
        String currentAccessToken = secondJson.path("data").path("accessToken").asText();

        ResponseEntity<String> logoutResponse = restTemplate.exchange(
            baseUrl + "/auth/logout",
            HttpMethod.POST,
            new HttpEntity<>(new com.genoutbound.gateway.security.dto.LogoutRequest(currentAccessToken, refreshToken)),
            String.class
        );
        assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpHeaders oldHeaders = new HttpHeaders();
        oldHeaders.setBearerAuth(firstAccessToken);
        ResponseEntity<String> revokedResponse = restTemplate.exchange(
            baseUrl + "/api/v1/scs/metrics",
            HttpMethod.GET,
            new HttpEntity<>(oldHeaders),
            String.class
        );
        assertThat(revokedResponse.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }
}
