package co.ke.tinker.auth;

import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.exception.AuthenticationException;
import co.ke.tinker.exception.NetworkException;
import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private final Configuration config;
    private final HttpClient httpClient;
    private String token;
    private Long expiresAt;

    public AuthenticationManager(Configuration config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public String getToken() {
        if (isTokenValid()) {
            return token;
        }
        return fetchToken();
    }

    private boolean isTokenValid() {
        if (token == null || expiresAt == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime < (expiresAt - 60);
    }

    private String fetchToken() {
        try {
            String credentials = config.getApiPublicKey() + ":" + config.getApiSecretKey();
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            String url = Endpoints.AUTH_TOKEN_URL;
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Accept", "application/json");

            String body = "credentials=" + java.net.URLEncoder.encode(encodedCredentials, StandardCharsets.UTF_8);

            HttpResponse response = httpClient.post(url, headers, body);
            Map<String, Object> result = response.getJson();

            if (response.getStatusCode() >= 400) {
                String message = result.containsKey("message") ? (String) result.get("message") : "Authentication failed";
                throw new AuthenticationException(message, ExceptionCode.AUTHENTICATION_ERROR);
            }

            if (!result.containsKey("token") || result.get("token") == null) {
                throw new NetworkException("Invalid authentication response: token missing", ExceptionCode.AUTHENTICATION_ERROR);
            }

            this.token = (String) result.get("token");
            Object expiresInObj = result.get("expires_in");
            int expiresIn = expiresInObj != null ? ((Number) expiresInObj).intValue() : 3600;
            this.expiresAt = (System.currentTimeMillis() / 1000) + expiresIn;

            return this.token;
        } catch (AuthenticationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Failed to authenticate: " + e.getMessage(), ExceptionCode.AUTHENTICATION_ERROR, e);
        }
    }
}

