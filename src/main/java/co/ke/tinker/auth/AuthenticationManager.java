package co.ke.tinker.auth;

import co.ke.tinker.config.Configuration;
import co.ke.tinker.exception.AuthenticationException;
import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.exception.NetworkException;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.http.HttpResponse;
import co.ke.tinker.model.ApiMeta;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private final Configuration config;
    private final HttpClient httpClient;
    private String token;
    private Long expiresAt;
    private ApiMeta lastMeta;

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

    public ApiMeta getLastMeta() {
        return lastMeta;
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

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Accept", "application/json");

            String body = "credentials=" + java.net.URLEncoder.encode(encodedCredentials, StandardCharsets.UTF_8);
            HttpResponse response = httpClient.post(config.getAuthUrl(), headers, body);
            Map<String, Object> result = response.getJson();
            Map<String, Object> authData = extractAuthData(result);

            if (response.getStatusCode() >= 400) {
                throw new AuthenticationException(extractErrorMessage(result), ExceptionCode.AUTHENTICATION_ERROR);
            }

            if (!authData.containsKey("token") || authData.get("token") == null) {
                throw new NetworkException("Invalid authentication response: token missing", ExceptionCode.AUTHENTICATION_ERROR);
            }

            this.token = (String) authData.get("token");
            Object expiresInObj = authData.get("expires_in");
            int expiresIn = expiresInObj != null ? ((Number) expiresInObj).intValue() : 3600;
            this.expiresAt = (System.currentTimeMillis() / 1000) + expiresIn;

            return this.token;
        } catch (AuthenticationException | NetworkException e) {
            throw e;
        } catch (Exception e) {
            throw new NetworkException("Failed to authenticate: " + e.getMessage(), ExceptionCode.AUTHENTICATION_ERROR, e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractAuthData(Map<String, Object> result) {
        if (result != null && result.containsKey("success")) {
            Object metaObj = result.get("meta");
            this.lastMeta = new ApiMeta(metaObj instanceof Map ? (Map<String, Object>) metaObj : null);

            if (Boolean.FALSE.equals(result.get("success"))) {
                throw new AuthenticationException(extractErrorMessage(result), ExceptionCode.AUTHENTICATION_ERROR);
            }

            Object dataObj = result.get("data");
            return dataObj instanceof Map ? (Map<String, Object>) dataObj : new HashMap<>();
        }

        return result != null ? result : new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    private String extractErrorMessage(Map<String, Object> result) {
        if (result != null) {
            Object errorObj = result.get("error");
            if (errorObj instanceof Map) {
                Map<String, Object> errorMap = (Map<String, Object>) errorObj;
                if (errorMap.containsKey("message")) {
                    return String.valueOf(errorMap.get("message"));
                }
                if (errorMap.containsKey("code")) {
                    return String.valueOf(errorMap.get("code"));
                }
            }
            if (result.containsKey("message")) {
                return String.valueOf(result.get("message"));
            }
        }
        return "Authentication failed";
    }
}
