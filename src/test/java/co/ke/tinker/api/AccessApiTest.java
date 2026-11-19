package co.ke.tinker.api;

import co.ke.tinker.Payments;
import co.ke.tinker.model.dto.InitiatePaymentRequestDto;
import co.ke.tinker.webhook.WebhookEvent;
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

class AccessApiTest {
    private HttpServer server;
    private Payments client;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<List<Object>> calls = new ArrayList<>();
    private String responseBody = "{\"success\":true,\"data\":{\"ok\":true}}";

    @BeforeEach
    void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body;
            if (exchange.getRequestURI().getPath().endsWith("/auth/token")) {
                body = "{\"token\":\"token\",\"expires_in\":3600}";
            } else {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                List<Object> call = new ArrayList<>();
                call.add(exchange.getRequestMethod());
                call.add(exchange.getRequestURI().toString().substring(4));
                call.add(requestBody.isEmpty() ? null : mapper.readValue(requestBody, Map.class));
                calls.add(call);
                assertEquals("Bearer token", exchange.getRequestHeaders().getFirst("Authorization"));
                body = responseBody;
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();
        client = new Payments("public", "secret", null, "http://127.0.0.1:" + server.getAddress().getPort());
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    private void expect(String method, String path, Map<String, Object> body) {
        List<Object> call = calls.get(calls.size() - 1);
        assertEquals(method, call.get(0));
        assertEquals(path, call.get(1));
        assertEquals(body, call.get(2));
    }

    @Test
    void customers() {
        client.customers().getCustomer("cus/1");
        expect("GET", "merchant/customers/cus%2F1", null);
        client.customers().getEntitlement("c1", "pro");
        expect("GET", "merchant/customers/c1/entitlements/pro", null);
        client.customers().grantPromotionalEntitlement("c1", "pro", "P7D", null);
        expect("POST", "merchant/customers/c1/entitlements/pro/promotional", Map.of("duration", "P7D"));
        client.customers().revokePromotionalEntitlements("c1", "pro");
        expect("POST", "merchant/customers/c1/entitlements/pro/revoke_promotionals", null);
        client.customers().alias("c1", "$anonymous:9");
        expect("POST", "merchant/customers/c1/alias", Map.of("alias", "$anonymous:9"));
        client.customers().transfer("c1", "c2");
        expect("POST", "merchant/customers/c1/transfer", Map.of("to", "c2"));
        client.customers().setAttributes("c1", Map.of("plan", "gold"));
        expect("POST", "merchant/customers/c1/attributes", Map.of("attributes", Map.of("plan", "gold")));
        client.customers().getOfferings("c1");
        expect("GET", "merchant/customers/c1/offerings", null);
        client.customers().createToken("c1", 120);
        expect("POST", "merchant/customers/c1/token", Map.of("expires_in", 120));
        client.customers().createToken("c1");
        expect("POST", "merchant/customers/c1/token", null);
    }

    @Test
    void entitlements() {
        responseBody = "{\"success\":true,\"data\":[{\"id\":\"e1\"}]}";
        assertEquals(1, client.entitlements().list().size());
        expect("GET", "merchant/entitlements", null);
        client.entitlements().create(Map.of("identifier", "pro"));
        expect("POST", "merchant/entitlements", Map.of("identifier", "pro"));
        client.entitlements().get("e1");
        expect("GET", "merchant/entitlements/e1", null);
        client.entitlements().update("e1", "Pro");
        expect("PATCH", "merchant/entitlements/e1", Map.of("display_name", "Pro"));
        client.entitlements().delete("e1");
        expect("DELETE", "merchant/entitlements/e1", null);
        client.entitlements().attachProducts("e1", List.of("p1"));
        expect("POST", "merchant/entitlements/e1/attach_products", Map.of("product_ids", List.of("p1")));
        client.entitlements().detachProducts("e1", List.of("p1"));
        expect("POST", "merchant/entitlements/e1/detach_products", Map.of("product_ids", List.of("p1")));
    }

    @Test
    void products() {
        client.products().list();
        expect("GET", "merchant/products", null);
        client.products().create(Map.of("name", "Pass"));
        expect("POST", "merchant/products", Map.of("name", "Pass"));
        client.products().get("p1");
        expect("GET", "merchant/products/p1", null);
        client.products().update("p1", Map.of("is_active", false));
        expect("PATCH", "merchant/products/p1", Map.of("is_active", false));
    }

    @Test
    void offerings() {
        client.offerings().list();
        expect("GET", "merchant/offerings", null);
        client.offerings().create(Map.of("identifier", "default"));
        expect("POST", "merchant/offerings", Map.of("identifier", "default"));
        client.offerings().get("o1");
        expect("GET", "merchant/offerings/o1", null);
        client.offerings().update("o1", Map.of("is_current", true));
        expect("PATCH", "merchant/offerings/o1", Map.of("is_current", true));
        client.offerings().delete("o1");
        expect("DELETE", "merchant/offerings/o1", null);
    }

    @Test
    void subscriptionRenewalPrompts() {
        client.subscriptions().listRenewalPrompts("s1");
        expect("GET", "merchant/subscriptions/renewal_prompts?subscription_id=s1", null);
        client.subscriptions().listRenewalPrompts();
        expect("GET", "merchant/subscriptions/renewal_prompts", null);
        client.subscriptions().sendRenewalPrompt("s1");
        expect("POST", "merchant/subscriptions/s1/renewal_prompt", null);
    }

    @Test
    void metricsExperimentsAndIntegrations() {
        client.metrics().get("2026-01-01", "2026-01-31");
        expect("GET", "merchant/metrics?from=2026-01-01&to=2026-01-31", null);
        client.metrics().get();
        expect("GET", "merchant/metrics", null);
        client.experiments().list();
        expect("GET", "merchant/experiments", null);
        client.experiments().create(Map.of("name", "A/B"));
        expect("POST", "merchant/experiments", Map.of("name", "A/B"));
        client.experiments().get("x1");
        expect("GET", "merchant/experiments/x1", null);
        client.experiments().stop("x1");
        expect("POST", "merchant/experiments/x1/stop", null);
        client.integrations().list();
        expect("GET", "merchant/integrations", null);
        client.integrations().put("slack", Map.of("enabled", true));
        expect("PUT", "merchant/integrations/slack", Map.of("enabled", true));
        client.integrations().test("slack");
        expect("POST", "merchant/integrations/slack/test", null);
        client.integrations().delete("slack");
        expect("DELETE", "merchant/integrations/slack", null);
    }

    @Test
    void initiateRequestCarriesProductFields() {
        Map<String, Object> map = new InitiatePaymentRequestDto(10.0, "KES", "mpesa", "R1", "https://x.test", null, null, null, null, null, null, null, "p1", "c1", "default").toMap();
        assertEquals("p1", map.get("productId"));
        assertEquals("c1", map.get("externalCustomerId"));
        assertEquals("default", map.get("presentedOfferingId"));
        assertFalse(new InitiatePaymentRequestDto(10.0, "KES", "mpesa", "R1", "https://x.test").toMap().containsKey("productId"));
    }

    @Test
    void parsesCustomerEntitlementAndSubscriptionEvents() {
        WebhookEvent transferred = new WebhookEvent(Map.of("id", "1", "type", "customer.transferred", "source", "customer",
                "data", Map.of("transferred_from", List.of("a"), "transferred_to", List.of("b"))));
        assertTrue(transferred.isCustomerEvent());
        assertEquals(List.of("b"), transferred.getCustomerData().getTransferredTo());

        WebhookEvent purchase = new WebhookEvent(Map.of("id", "2", "type", "purchase.completed", "source", "payment",
                "data", Map.of("product_id", "p1", "external_customer_id", "c1", "entitlement_ids", List.of("pro"), "amount", 5)));
        assertTrue(purchase.isEntitlementEvent());
        assertEquals("p1", purchase.getEntitlementData().getProductId());
        assertNull(purchase.toTransaction());

        WebhookEvent granted = new WebhookEvent(Map.of("id", "3", "type", "entitlement.granted", "source", "entitlement",
                "data", Map.of("id", "g1", "external_customer_id", "c1")));
        assertEquals("g1", granted.getEntitlementData().getId());

        WebhookEvent issue = new WebhookEvent(Map.of("id", "4", "type", "subscription.billing_issue", "source", "subscription",
                "data", Map.of("subscription_id", "s1", "product_id", "plan", "entitlement_ids", List.of("pro"), "external_customer_id", "ext1",
                        "period_type", "NORMAL", "grace_period_expiration_at", "2026-02-01T00:00:00Z")));
        assertEquals("plan", issue.getSubscriptionData().getProductId());
        assertEquals("ext1", issue.getSubscriptionData().getExternalCustomerId());
        assertEquals("2026-02-01T00:00:00Z", issue.getSubscriptionData().getGracePeriodExpirationAt());
    }
}
