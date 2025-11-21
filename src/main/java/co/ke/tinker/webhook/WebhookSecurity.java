package co.ke.tinker.webhook;

import java.util.Map;

public class WebhookSecurity {
    private final String signature;
    private final String algorithm;

    public WebhookSecurity(Map<String, Object> security) {
        if (security != null) {
            this.signature = security.containsKey("signature") ? (String) security.get("signature") : "";
            this.algorithm = security.containsKey("algorithm") ? (String) security.get("algorithm") : "HMAC-SHA256";
        } else {
            this.signature = "";
            this.algorithm = "HMAC-SHA256";
        }
    }

    public String getSignature() {
        return signature;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}

