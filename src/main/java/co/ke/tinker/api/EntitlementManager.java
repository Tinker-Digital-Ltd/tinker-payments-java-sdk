package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.List;
import java.util.Map;

public class EntitlementManager extends BaseManager {
    public EntitlementManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public List<Map<String, Object>> list() {
        return requestList(Endpoints.ENTITLEMENTS_PATH);
    }

    public Map<String, Object> create(Map<String, Object> request) {
        return request("POST", Endpoints.ENTITLEMENTS_PATH, request);
    }

    public Map<String, Object> get(String id) {
        return request("GET", path(id), null);
    }

    public Map<String, Object> update(String id, Map<String, Object> request) {
        return request("PATCH", path(id), request);
    }

    public Map<String, Object> delete(String id) {
        return request("DELETE", path(id), null);
    }

    public Map<String, Object> attachProducts(String id, List<String> productIds) {
        return request("POST", path(id) + "/attach_products", Map.of("product_ids", productIds));
    }

    public Map<String, Object> detachProducts(String id, List<String> productIds) {
        return request("POST", path(id) + "/detach_products", Map.of("product_ids", productIds));
    }

    private String path(String id) {
        return Endpoints.ENTITLEMENTS_PATH + "/" + escapePath(id);
    }
}
