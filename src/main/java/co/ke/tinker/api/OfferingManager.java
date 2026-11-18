package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.List;
import java.util.Map;

public class OfferingManager extends BaseManager {
    public OfferingManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public List<Map<String, Object>> list() {
        return requestList(Endpoints.OFFERINGS_PATH);
    }

    public Map<String, Object> create(Map<String, Object> request) {
        return request("POST", Endpoints.OFFERINGS_PATH, request);
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

    private String path(String id) {
        return Endpoints.OFFERINGS_PATH + "/" + escapePath(id);
    }
}
