package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class CustomerManager extends BaseManager {
    public CustomerManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public Map<String, Object> getCustomer(String externalCustomerId) {
        return request("GET", path(externalCustomerId), null);
    }

    public Map<String, Object> getEntitlement(String externalCustomerId, String identifier) {
        return request("GET", entitlementPath(externalCustomerId, identifier), null);
    }

    public Map<String, Object> grantPromotionalEntitlement(String externalCustomerId, String identifier, String duration, String endTime) {
        Map<String, Object> body = new HashMap<>();
        if (duration != null) body.put("duration", duration);
        if (endTime != null) body.put("end_time", endTime);
        return request("POST", entitlementPath(externalCustomerId, identifier) + "/promotional", body);
    }

    public Map<String, Object> revokePromotionalEntitlements(String externalCustomerId, String identifier) {
        return request("POST", entitlementPath(externalCustomerId, identifier) + "/revoke_promotionals", null);
    }

    public Map<String, Object> alias(String externalCustomerId, String alias) {
        return request("POST", path(externalCustomerId) + "/alias", Map.of("alias", alias));
    }

    public Map<String, Object> transfer(String externalCustomerId, String to) {
        return request("POST", path(externalCustomerId) + "/transfer", Map.of("to", to));
    }

    public Map<String, Object> setAttributes(String externalCustomerId, Map<String, String> attributes) {
        return request("POST", path(externalCustomerId) + "/attributes", Map.of("attributes", attributes));
    }

    public Map<String, Object> getOfferings(String externalCustomerId) {
        return request("GET", path(externalCustomerId) + "/offerings", null);
    }

    public Map<String, Object> createToken(String externalCustomerId, Integer expiresIn) {
        Map<String, Object> body = new HashMap<>();
        if (expiresIn != null) body.put("expires_in", expiresIn);
        return request("POST", path(externalCustomerId) + "/token", body);
    }

    public Map<String, Object> createToken(String externalCustomerId) {
        return createToken(externalCustomerId, null);
    }

    private String path(String externalCustomerId) {
        return Endpoints.CUSTOMERS_PATH + "/" + escapePath(externalCustomerId);
    }

    private String entitlementPath(String externalCustomerId, String identifier) {
        return path(externalCustomerId) + "/entitlements/" + escapePath(identifier);
    }
}
