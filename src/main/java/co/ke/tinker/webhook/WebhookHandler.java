package co.ke.tinker.webhook;

import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.exception.WebhookException;
import co.ke.tinker.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebhookHandler {
    private final ObjectMapper objectMapper;

    public WebhookHandler() {
        this.objectMapper = new ObjectMapper();
    }

    public WebhookEvent handle(String payload) {
        Map<String, Object> data;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> parsed = objectMapper.readValue(payload, Map.class);
            data = parsed;
        } catch (Exception e) {
            throw new InvalidPayloadException("Invalid JSON payload: " + e.getMessage());
        }

        if (!(data instanceof Map)) {
            throw new InvalidPayloadException("Webhook payload must be a hash");
        }

        return new WebhookEvent(data);
    }

    public WebhookEvent handle(Map<String, Object> payload) {
        if (payload == null) {
            throw new InvalidPayloadException("Webhook payload must be a hash");
        }
        return new WebhookEvent(payload);
    }

    public WebhookEvent handleFromRequest(String requestBody) {
        if (requestBody == null || requestBody.isEmpty()) {
            throw new WebhookException("Unable to read request body");
        }
        return handle(requestBody);
    }

    public Transaction handleAsTransaction(String payload) {
        WebhookEvent event = handle(payload);
        return event.toTransaction();
    }

    public Transaction handleAsTransaction(Map<String, Object> payload) {
        WebhookEvent event = handle(payload);
        return event.toTransaction();
    }

    public boolean verifySignature(WebhookEvent event, String webhookSecret) {
        if (webhookSecret == null || webhookSecret.isEmpty()) {
            return false;
        }

        String signature = event.getSecurity().getSignature();
        if (signature == null || !signature.startsWith("sha256=")) {
            return false;
        }

        try {
            Map<String, Object> payloadWithoutSecurity = new LinkedHashMap<>();
            payloadWithoutSecurity.put("id", event.getId());
            payloadWithoutSecurity.put("type", event.getType());
            payloadWithoutSecurity.put("source", event.getSource());
            payloadWithoutSecurity.put("timestamp", event.getTimestamp());
            payloadWithoutSecurity.put("data", event.getRawData());
            payloadWithoutSecurity.put("meta", event.getRawMeta());

            String encoded = objectMapper.writeValueAsString(payloadWithoutSecurity);
            String computed = "sha256=" + hmacSha256Hex(encoded, webhookSecret);
            return signature.equals(computed);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifySignature(String payload, String webhookSecret) {
        return verifySignature(handle(payload), webhookSecret);
    }

    public boolean verifySignature(Map<String, Object> payload, String webhookSecret) {
        return verifySignature(handle(payload), webhookSecret);
    }

    private String hmacSha256Hex(String payload, String secret) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hash = sha256Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
