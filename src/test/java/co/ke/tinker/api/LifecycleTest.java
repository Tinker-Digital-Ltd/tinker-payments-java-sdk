package co.ke.tinker.api;

import co.ke.tinker.Payments;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.model.lifecycle.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.*;

class LifecycleTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode row;
    private int calls;
    private String actualMethod, actualPath, actualKey, actualAuthorization, actualBody;

    private JsonNode fixture() throws Exception {
        JsonNode snapshot = mapper.readTree(getClass().getResourceAsStream("/merchant-lifecycle.json"));
        String override = System.getenv("TINKER_LIFECYCLE_FIXTURE");
        Path canonical = Path.of(override == null ? "../tinker-payments-openapi/fixtures/merchant-lifecycle.json" : override);
        if (Files.exists(canonical)) assertEquals(snapshot, mapper.readTree(canonical.toFile()), "Shared fixture snapshot drifted");
        return snapshot;
    }

    @Test
    void followsEverySharedRequestResponseAndErrorContract() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body;
            int status;
            if (exchange.getRequestURI().getPath().endsWith("/auth/token")) {
                body = "{\"token\":\"token\",\"expires_in\":3600}"; status = 200;
            } else {
                calls++;
                actualMethod = exchange.getRequestMethod(); actualPath = exchange.getRequestURI().toString().substring(4);
                actualBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                actualKey = exchange.getRequestHeaders().getFirst("Idempotency-Key");
                actualAuthorization = exchange.getRequestHeaders().getFirst("Authorization");
                body = row.get("response").toString(); status = row.get("status").asInt();
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8); exchange.sendResponseHeaders(status, bytes.length);
            exchange.getResponseBody().write(bytes); exchange.close();
        });
        server.start();
        try {
            Payments client = new Payments("public", "secret", null, "http://127.0.0.1:" + server.getAddress().getPort());
            for (JsonNode scenario : fixture().get("cases")) {
                row = scenario; calls = 0;
                Object result = invoke(client, row.get("operation").asText());
                assertJson(row.get("response").get("data"), mapper.valueToTree(result));
                assertEquals(1, calls); assertEquals(row.get("method").asText(), actualMethod);
                URI actual = URI.create(actualPath), expected = URI.create(row.get("path").asText());
                assertEquals(expected.getRawPath(), actual.getRawPath()); assertEquals(query(expected), query(actual));
                assertJson(row.get("body"), actualBody.isEmpty() ? mapper.nullNode() : mapper.readTree(actualBody));
                assertEquals("Bearer token", actualAuthorization);
                assertEquals(row.get("headers").path("Idempotency-Key").isMissingNode() ? null : "stable-key", actualKey);
            }
            for (JsonNode scenario : fixture().get("errors")) {
                row = scenario;
                ApiException error = assertThrows(ApiException.class, () -> client.transactions().cancelCheckout("payment", new CancellationRequest("stop"), "key"));
                assertEquals(row.get("status").asInt(), error.getHttpStatus());
                assertEquals(row.at("/response/error/code").asText(), error.getErrorCode());
                assertJson(row.get("response"), mapper.valueToTree(error.getDetails()));
            }
        } finally { server.stop(0); }
    }

    private Object invoke(Payments client, String operation) {
        TransactionManager tx = client.transactions(); PayoutManager payouts = client.payouts();
        switch (operation) {
            case "refund": RefundRequest refund = new RefundRequest(RefundReason.REQUESTED_BY_CUSTOMER); refund.amount = 100.0; return tx.refund("payment/a b", refund, " stable-key ");
            case "refunds": return tx.refunds("payment/a b");
            case "get_refund": return tx.getRefund("payment/a b", "refund/a b");
            case "payout_methods": return payouts.methods();
            case "payout_quote": PayoutQuoteRequest quote = new PayoutQuoteRequest("method/a &b", 100.0); quote.currency = "JPY"; return payouts.quote(quote);
            case "request_payout": return payouts.requestPayout(new PayoutRequest("method/a &b", 100.0, "JPY", 2.0), "stable-key");
            case "payouts": PayoutFilters filters = new PayoutFilters(); filters.page = 2; filters.perPage = 10; filters.status = PayoutStatus.PENDING_APPROVAL; filters.currency = "JPY"; return payouts.list(filters);
            case "get_payout": return payouts.get("payout/a b");
            case "cancel_checkout": return tx.cancelCheckout("payment/a b", new CancellationRequest("Customer left checkout"), "stable-key");
            case "cancellations": return tx.cancellations("payment/a b");
            case "get_cancellation": return tx.getCancellation("payment/a b", "cancel/a b");
            default: throw new AssertionError("Unhandled fixture " + operation);
        }
    }

    @Test
    void rejectsInvalidMoneyAndKeysBeforeTransport() {
        Payments client = new Payments("public", "secret");
        for (String key : new String[] {"", " ", "é".repeat(128)}) assertThrows(IllegalArgumentException.class, () -> client.transactions().cancelCheckout("payment", new CancellationRequest("stop"), key));
        for (double amount : new double[] {Double.NaN, Double.POSITIVE_INFINITY, 0, -1}) assertThrows(IllegalArgumentException.class, () -> client.payouts().quote(new PayoutQuoteRequest("method", amount)));
    }

    private Map<String, String> query(URI uri) {
        Map<String, String> result = new TreeMap<>();
        if (uri.getRawQuery() != null) for (String entry : uri.getRawQuery().split("&")) {
            String[] parts = entry.split("=", 2); String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            if (parts[0].equals("amount")) value = Double.toString(Double.parseDouble(value));
            result.put(parts[0], value);
        }
        return result;
    }

    private void assertJson(JsonNode expected, JsonNode actual) {
        if (expected.isNumber()) { assertEquals(expected.asDouble(), actual.asDouble()); return; }
        if (expected.isObject()) {
            expected.fields().forEachRemaining(field -> { assertTrue(actual.has(field.getKey()), field.getKey()); assertJson(field.getValue(), actual.get(field.getKey())); }); return;
        }
        if (expected.isArray()) { assertEquals(expected.size(), actual.size()); for (int i = 0; i < expected.size(); i++) assertJson(expected.get(i), actual.get(i)); return; }
        assertEquals(expected, actual);
    }
}
