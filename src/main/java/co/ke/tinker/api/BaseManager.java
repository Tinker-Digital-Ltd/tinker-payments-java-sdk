package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.exception.NetworkException;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.http.HttpResponse;
import co.ke.tinker.model.ApiMeta;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class BaseManager {
    protected final Configuration config;
    protected final HttpClient httpClient;
    protected final AuthenticationManager authManager;
    private final ObjectMapper objectMapper;
    protected ApiMeta lastMeta;

    public BaseManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        this.config = config;
        this.httpClient = httpClient;
        this.authManager = authManager;
        this.objectMapper = new ObjectMapper();
    }

    public ApiMeta getLastMeta() {
        return lastMeta;
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

        HttpResponse response = "GET".equalsIgnoreCase(method)
                ? httpClient.get(url, headers)
                : httpClient.post(url, headers, body);

        Map<String, Object> result = response.getJson();

        if (response.getStatusCode() >= 400) {
            throw new ApiException(extractErrorMessage(result), ExceptionCode.API_ERROR);
        }

        if (result != null && result.containsKey("success")) {
            Object metaObj = result.get("meta");
            @SuppressWarnings("unchecked")
            Map<String, Object> metaMap = metaObj instanceof Map ? (Map<String, Object>) metaObj : null;
            this.lastMeta = new ApiMeta(metaMap);

            if (Boolean.FALSE.equals(result.get("success"))) {
                throw new ApiException(extractErrorMessage(result), ExceptionCode.API_ERROR);
            }

            Object dataObj = result.get("data");
            if (dataObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) dataObj;
                return map;
            }
            Map<String, Object> wrapped = new HashMap<>();
            wrapped.put("value", dataObj);
            return wrapped;
        }

        return result != null ? result : new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    protected String extractErrorMessage(Map<String, Object> result) {
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

        return "Unknown error";
    }
}
