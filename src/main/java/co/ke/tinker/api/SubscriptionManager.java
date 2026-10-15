package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.model.dto.CreateSubscriptionPlanRequestDto;
import co.ke.tinker.model.dto.CreateSubscriptionRequestDto;
import co.ke.tinker.model.dto.ManagedCheckout;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SubscriptionManager extends BaseManager {
    public SubscriptionManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public Map<String, Object> createPlan(CreateSubscriptionPlanRequestDto request) {
        return request("POST", Endpoints.SUBSCRIPTION_PLANS_PATH, request.toMap());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> listPlans() {
        Map<String, Object> wrapped = request("GET", Endpoints.SUBSCRIPTION_PLANS_PATH, null);
        Object value = wrapped.get("value");
        return value instanceof List ? (List<Map<String, Object>>) value : List.of();
    }

    public Map<String, Object> create(CreateSubscriptionRequestDto request) {
        return request("POST", Endpoints.SUBSCRIPTION_BASE_PATH, request.toMap());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> list(String planId, String externalCustomerId) {
        String endpoint = Endpoints.SUBSCRIPTION_BASE_PATH;
        StringBuilder query = new StringBuilder();
        if (planId != null && !planId.trim().isEmpty()) {
            query.append("plan_id=").append(URLEncoder.encode(planId, StandardCharsets.UTF_8));
        }
        if (externalCustomerId != null && !externalCustomerId.trim().isEmpty()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append("external_customer_id=").append(URLEncoder.encode(externalCustomerId, StandardCharsets.UTF_8));
        }
        if (query.length() > 0) {
            endpoint += "?" + query;
        }

        Map<String, Object> wrapped = request("GET", endpoint, null);
        Object value = wrapped.get("value");
        return value instanceof List ? (List<Map<String, Object>>) value : List.of();
    }

    public List<Map<String, Object>> list() {
        return list(null, null);
    }

    public Map<String, Object> cancel(String subscriptionId) {
        return request("POST", Endpoints.SUBSCRIPTION_BASE_PATH + "/" + subscriptionId + "/cancel", null);
    }
    public Map<String, Object> checkout(ManagedCheckout input) {
        return request("POST", "merchant/billing/checkout", input.toMap());
    }

    public Map<String, Object> billingResource(String id) {
        return request("GET", billingResourcePath(id), null);
    }

    public Map<String, Object> changeQuantity(String id, int quantity, String key) {
        return request("POST", billingResourcePath(id) + "/quantity", Map.of("quantity", quantity, "idempotency_key", key));
    }

    public Map<String, Object> scheduleQuantity(String id, int quantity, String key) {
        return request("POST", billingResourcePath(id) + "/schedule", Map.of("quantity", quantity, "idempotency_key", key));
    }

    public Map<String, Object> clearScheduledQuantity(String id, String key) {
        return request("POST", billingResourcePath(id) + "/unschedule", Map.of("idempotency_key", key));
    }

    public Map<String, Object> setCancelAtRenewal(String id, boolean cancel, String key) {
        return request("POST", billingResourcePath(id) + "/cancel", Map.of("cancel", cancel, "idempotency_key", key));
    }

    public Map<String, Object> billingOperation(String key) {
        return request("GET", "merchant/billing/operations/" + escapePath(key), null);
    }

    public Map<String, Object> paymentMethodPortal(String id, String returnUrl, String key) {
        return request("POST", billingResourcePath(id) + "/payment-method", Map.of("return_url", returnUrl, "idempotency_key", key));
    }

    public Map<String, Object> billingPaymentHistory(String id, String cursor, long snapshot) {
        StringBuilder query = new StringBuilder();
        if (!cursor.isEmpty()) {
            query.append("starting_after=").append(escapePath(cursor));
        }
        if (snapshot > 0) {
            if (query.length() > 0) query.append("&");
            query.append("snapshot=").append(snapshot);
        }
        String endpoint = billingResourcePath(id) + "/payment-history";
        if (query.length() > 0) endpoint += "?" + query;
        return request("GET", endpoint, null);
    }

    public Map<String, Object> billingPaymentHistory(String id) {
        return billingPaymentHistory(id, "", 0);
    }

    private String billingResourcePath(String id) {
        return "merchant/billing/resources/" + escapePath(id);
    }

    private String escapePath(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
