package co.ke.tinker.webhook;

import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.exception.WebhookException;
import co.ke.tinker.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}

