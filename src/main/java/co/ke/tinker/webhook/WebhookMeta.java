package co.ke.tinker.webhook;

import java.util.Map;

public class WebhookMeta {
    private final String version;
    private final String appId;
    private final String gateway;

    public WebhookMeta(Map<String, Object> meta) {
        if (meta != null) {
            this.version = meta.containsKey("version") ? (String) meta.get("version") : "1.0";
            this.appId = meta.containsKey("app_id") ? (String) meta.get("app_id") : "";
            this.gateway = meta.containsKey("gateway") ? (String) meta.get("gateway") : null;
        } else {
            this.version = "1.0";
            this.appId = "";
            this.gateway = null;
        }
    }

    public String getVersion() {
        return version;
    }

    public String getAppId() {
        return appId;
    }

    public String getGateway() {
        return gateway;
    }
}

