package co.ke.tinker.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.exception.ExceptionCode;

import java.io.IOException;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String body;
    private final Map<String, java.util.List<String>> headers;
    private final ObjectMapper objectMapper;

    public HttpResponse(int statusCode, String body, Map<String, java.util.List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.objectMapper = new ObjectMapper();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, java.util.List<String>> getHeaders() {
        return headers;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getJson() {
        try {
            return (Map<String, Object>) objectMapper.readValue(body, Map.class);
        } catch (IOException e) {
            throw new InvalidPayloadException("Invalid JSON response: " + e.getMessage(), ExceptionCode.INVALID_PAYLOAD);
        }
    }
}

