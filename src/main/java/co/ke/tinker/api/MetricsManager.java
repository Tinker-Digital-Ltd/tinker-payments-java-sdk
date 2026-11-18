package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.config.Endpoints;
import co.ke.tinker.http.HttpClient;

import java.util.Map;

public class MetricsManager extends BaseManager {
    public MetricsManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public Map<String, Object> get(String from, String to) {
        StringBuilder query = new StringBuilder();
        appendParam(query, "from", from);
        appendParam(query, "to", to);
        return request("GET", Endpoints.METRICS_PATH + (query.length() > 0 ? "?" + query : ""), null);
    }

    public Map<String, Object> get() {
        return get(null, null);
    }
}
