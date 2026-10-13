package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ManagedBillingTest {
    private HttpServer server;
    private SubscriptionManager manager;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<List<Object>> calls = new ArrayList<>();
    private int status = 200;
    private String responseBody = "{\"success\":true,\"data\":{\"snapshot\":123,\"next_cursor\":\"in_next\",\"has_more\":true}}";

    @BeforeEach
    void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body;
            int responseStatus;
            if (exchange.getRequestURI().getPath().endsWith("/auth/token")) {
                body = "{\"token\":\"token\",\"expires_in\":3600}";
                responseStatus = 200;
            } else {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                List<Object> call = new ArrayList<>();
                call.add(exchange.getRequestMethod());
                call.add(exchange.getRequestURI().toString().substring(4));
                call.add(requestBody.isEmpty() ? null : mapper.readValue(requestBody, Map.class));
                calls.add(call);
                assertEquals("Bearer token", exchange.getRequestHeaders().getFirst("Authorization"));
                body = responseBody;
                responseStatus = status;
            }
            exchange.getResponseHeaders().add("x-request-id", "header-id");
            exchange.getResponseHeaders().add("rEtRy-AfTeR", "30");
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseStatus, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();
        Configuration config = new Configuration("public", "secret", "http://127.0.0.1:" + server.getAddress().getPort());
        HttpClient http = new HttpClient();
        manager = new SubscriptionManager(config, http, new AuthenticationManager(config, http));
    }

    @AfterEach
    void tearDown() {
        if (server != null) server.stop(0);
    }

    @Test
    void retainsRecoveryMetadataForHttpAndEnvelopeFailures() {
        for (int responseStatus : new int[] {429, 200, 502}) {
            status = responseStatus;
            responseBody = status == 502 ? "<html>Bad gateway</html>" : "{\"success\":false,\"error\":{\"message\":\"Try later\",\"code\":\"RATE_LIMITED\",\"provider_code\":\"rate_limit\",\"request_id\":\"body-id\",\"outcome\":\"not_applied\"}}";
            ApiException error = assertThrows(ApiException.class, () -> manager.listPlans());
            assertEquals(status, error.getHttpStatus());
            assertEquals("30", error.getRetryAfter());
            assertEquals(status == 502 ? "header-id" : "body-id", error.getRequestId());
            assertEquals(status == 502 ? null : "RATE_LIMITED", error.getErrorCode());
            assertEquals(status == 502 ? null : "rate_limit", error.getProviderCode());
            assertEquals(status == 502 ? null : "not_applied", error.getOutcome());
        }
    }

    @Test
    void retainsLegacyExceptions() {
        ApiException error = new ApiException("legacy", 123);
        assertEquals(123, error.getCode());
        assertEquals("legacy", error.getMessage());
    }

    private List<Object> call(String method, String path, Object body) {
        List<Object> call = new ArrayList<>();
        call.add(method);
        call.add(path);
        call.add(body);
        return call;
    }
}
