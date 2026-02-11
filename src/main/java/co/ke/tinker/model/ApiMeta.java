package co.ke.tinker.model;

import java.util.HashMap;
import java.util.Map;

public class ApiMeta {
    private final String requestId;
    private final String timestamp;
    private final String environment;

    public ApiMeta(Map<String, Object> meta) {
        this.requestId = meta != null && meta.get("request_id") != null ? String.valueOf(meta.get("request_id")) : null;
        this.timestamp = meta != null && meta.get("timestamp") != null ? String.valueOf(meta.get("timestamp")) : null;
        this.environment = meta != null && meta.get("environment") != null ? String.valueOf(meta.get("environment")) : null;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEnvironment() {
        return environment;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("request_id", requestId);
        map.put("timestamp", timestamp);
        map.put("environment", environment);
        return map;
    }
}
