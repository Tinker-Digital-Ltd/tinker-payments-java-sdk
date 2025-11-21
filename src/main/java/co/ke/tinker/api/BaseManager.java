package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.exception.NetworkException;
import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class BaseManager {
    protected final Configuration config;
    protected final HttpClient httpClient;
    protected final AuthenticationManager authManager;
    private final ObjectMapper objectMapper;

    public BaseManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        this.config = config;
        this.httpClient = httpClient;
        this.authManager = authManager;
        this.objectMapper = new ObjectMapper();
    }

    protected Map<String, Object> request(String method, String endpoint, Map<String, Object> data) {
        String baseUrl = config.getBaseUrl().replaceAll("/$", "");
        if (endpoint.startsWith("/")) {
            endpoint = endpoint.substring(1);
        }
        String url = baseUrl + "/" + endpoint;

        String token = authManager.getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        String body = null;
        if (data != null && !data.isEmpty()) {
            try {
                body = objectMapper.writeValueAsString(data);
            } catch (Exception e) {
                throw new NetworkException("Failed to serialize request data: " + e.getMessage(), ExceptionCode.NETWORK_ERROR, e);
            }
        }

        HttpResponse response = httpClient.post(url, headers, body);
        Map<String, Object> result = response.getJson();

        if (response.getStatusCode() >= 400) {
            String message = "Unknown error";
            if (result instanceof Map) {
                if (result.containsKey("message")) {
                    message = (String) result.get("message");
                } else if (result.containsKey("error")) {
                    message = (String) result.get("error");
                }
            }
            throw new ApiException(message, ExceptionCode.API_ERROR);
        }

        return result != null ? result : new HashMap<>();
    }
}

