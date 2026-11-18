package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.List;
import java.util.Map;

public class IntegrationManager extends BaseManager {
    public IntegrationManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public List<Map<String, Object>> list() {
        return requestList(Endpoints.INTEGRATIONS_PATH);
    }

    public Map<String, Object> put(String kind, Map<String, Object> request) {
        return request("PUT", path(kind), request);
    }

    public Map<String, Object> delete(String kind) {
        return request("DELETE", path(kind), null);
    }

    public Map<String, Object> test(String kind) {
        return request("POST", path(kind) + "/test", null);
    }

    private String path(String kind) {
        return Endpoints.INTEGRATIONS_PATH + "/" + escapePath(kind);
    }
}
