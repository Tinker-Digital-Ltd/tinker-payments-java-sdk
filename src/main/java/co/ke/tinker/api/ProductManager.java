package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.List;
import java.util.Map;

public class ProductManager extends BaseManager {
    public ProductManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public List<Map<String, Object>> list() {
        return requestList(Endpoints.PRODUCTS_PATH);
    }

    public Map<String, Object> create(Map<String, Object> request) {
        return request("POST", Endpoints.PRODUCTS_PATH, request);
    }

    public Map<String, Object> get(String id) {
        return request("GET", path(id), null);
    }

    public Map<String, Object> update(String id, Map<String, Object> request) {
        return request("PATCH", path(id), request);
    }

    private String path(String id) {
        return Endpoints.PRODUCTS_PATH + "/" + escapePath(id);
    }
}
