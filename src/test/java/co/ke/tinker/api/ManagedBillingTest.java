package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.model.dto.ManagedCheckout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
    void sendsEveryManagedOperationWithExactPayloadsAndEscapedIds() throws Exception {
        ManagedCheckout checkout = new ManagedCheckout("workspace", "buyer@example.com", "Buyer", "EUR", 15000L, 3, "year", "https://example.com/return", "checkout-key");
        assertEquals(Map.of("external_customer_id", "workspace", "email", "buyer@example.com", "name", "Buyer",
                "currency", "EUR", "unit_amount", 15000L, "quantity", 3, "interval", "year",
                "return_url", "https://example.com/return", "idempotency_key", "checkout-key"), checkout.toMap());
        manager.checkout(checkout);
        manager.expireCheckout("cs/a b", "expire-key-123456");
        manager.billingResource("sub/a b");
        manager.changeQuantity("sub/a b", 4, "change-key");
        manager.scheduleQuantity("sub/a b", 2, "schedule-key");
        manager.clearScheduledQuantity("sub/a b", "clear-key");
        manager.setCancelAtRenewal("sub/a b", false, "resume-key");
        manager.billingOperation("key/a b");
        manager.paymentMethodPortal("sub/a b", "https://example.com/return", "portal-key");
        Map<String, Object> page = manager.billingPaymentHistory("sub/a b", "in/a &b", 123);
        manager.billingPaymentHistory("sub/a b");
        String base = "merchant/billing/resources/sub%2Fa%20b";
        List<List<Object>> expected = new ArrayList<>();
        expected.add(call("POST", "merchant/billing/checkout", mapper.readValue(mapper.writeValueAsString(checkout.toMap()), Map.class)));
        expected.add(call("POST", "merchant/billing/checkout/cs%2Fa%20b/expire", Map.of("idempotency_key", "expire-key-123456")));
        expected.add(call("GET", base, null));
        expected.add(call("POST", base + "/quantity", Map.of("quantity", 4, "idempotency_key", "change-key")));
        expected.add(call("POST", base + "/schedule", Map.of("quantity", 2, "idempotency_key", "schedule-key")));
        expected.add(call("POST", base + "/unschedule", Map.of("idempotency_key", "clear-key")));
        expected.add(call("POST", base + "/cancel", Map.of("cancel", false, "idempotency_key", "resume-key")));
        expected.add(call("GET", "merchant/billing/operations/key%2Fa%20b", null));
        expected.add(call("POST", base + "/payment-method", Map.of("return_url", "https://example.com/return", "idempotency_key", "portal-key")));
        expected.add(call("GET", base + "/payment-history?starting_after=in%2Fa%20%26b&snapshot=123", null));
        expected.add(call("GET", base + "/payment-history", null));
        assertEquals(expected, calls);
        assertEquals(Map.of("snapshot", 123, "next_cursor", "in_next", "has_more", true), page);
    }

    @Test
    void sendsTrialDaysOnlyWhenPositive() throws Exception {
        manager.checkout(new ManagedCheckout("workspace", "email", "name", "EUR", 15000L, 1, "month", "return", "key"));
        manager.checkout(new ManagedCheckout("workspace", "email", "name", "EUR", 15000L, 1, "month", "return", "key", 14));
        assertFalse(((Map<?, ?>) calls.get(0).get(2)).containsKey("trial_days"));
        assertEquals(14, ((Map<?, ?>) calls.get(1).get(2)).get("trial_days"));
    }

    @Test
    void sendsTrialEndOnlyWhenSet() throws Exception {
        manager.checkout(new ManagedCheckout("workspace", "email", "name", "EUR", 15000L, 1, "month", "return", "key"));
        manager.checkout(new ManagedCheckout("workspace", "email", "name", "EUR", 15000L, 1, "month", "return", "key", 0, Instant.ofEpochSecond(1900000000L)));
        assertFalse(((Map<?, ?>) calls.get(0).get(2)).containsKey("trial_end"));
        assertEquals(1900000000L, ((Number) ((Map<?, ?>) calls.get(1).get(2)).get("trial_end")).longValue());
    }

    @Test
    void sendsTrialEndAndEndTrialOperations() throws Exception {
        manager.setTrialEnd("sub/a b", Instant.ofEpochSecond(1900000000L), "trial-key");
        manager.endTrial("sub/a b", "end-key");
        String base = "merchant/billing/resources/sub%2Fa%20b";
        assertEquals(call("POST", base + "/trial-end", Map.of("trial_end", 1900000000, "idempotency_key", "trial-key")), calls.get(0));
        assertEquals(call("POST", base + "/end-trial", Map.of("idempotency_key", "end-key")), calls.get(1));
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
    void retainsLegacyExceptionsAndLargeIntegerPrices() {
        ApiException error = new ApiException("legacy", 123);
        assertEquals(123, error.getCode());
        assertEquals("legacy", error.getMessage());
        ManagedCheckout checkout = new ManagedCheckout("workspace", "email", "name", "EUR", 9007199254740993L, 3, "year", "return", "key");
        assertEquals(9007199254740993L, checkout.toMap().get("unit_amount"));
    }

    private List<Object> call(String method, String path, Object body) {
        List<Object> call = new ArrayList<>();
        call.add(method);
        call.add(path);
        call.add(body);
        return call;
    }
}
