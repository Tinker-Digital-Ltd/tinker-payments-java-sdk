package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.model.dto.CreateSubscriptionPlanRequestDto;
import co.ke.tinker.model.dto.CreateSubscriptionRequestDto;

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
}
