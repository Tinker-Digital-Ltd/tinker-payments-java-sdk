package co.ke.tinker.webhook;

import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WebhookHandlerTest {
    private final WebhookHandler handler = new WebhookHandler();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testHandleJsonStringPayload() throws Exception {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("id", "evt_123");
        payloadMap.put("type", "payment.completed");
        payloadMap.put("source", "payment");
        payloadMap.put("timestamp", "2024-01-01T00:00:00Z");

        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("status", "success");
        data.put("reference", "REF-123");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("channel", "mpesa");
        data.put("created_at", "2024-01-01T00:00:00Z");
        payloadMap.put("data", data);

        Map<String, Object> meta = new HashMap<>();
        meta.put("app_id", "app_123");
        payloadMap.put("meta", meta);

        Map<String, Object> security = new HashMap<>();
        security.put("signature", "sig_123");
        payloadMap.put("security", security);

        String payload = objectMapper.writeValueAsString(payloadMap);
        WebhookEvent event = handler.handle(payload);

        assertNotNull(event);
        assertTrue(event.isPaymentEvent());
        assertEquals("evt_123", event.getId());
        assertEquals("payment.completed", event.getType());
    }

    @Test
    void testHandleMapPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", "evt_123");
        payload.put("type", "payment.completed");
        payload.put("source", "payment");
        payload.put("timestamp", "2024-01-01T00:00:00Z");

        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("status", "success");
        data.put("reference", "REF-123");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("channel", "mpesa");
        data.put("created_at", "2024-01-01T00:00:00Z");
        payload.put("data", data);

        Map<String, Object> meta = new HashMap<>();
        meta.put("app_id", "app_123");
        payload.put("meta", meta);

        Map<String, Object> security = new HashMap<>();
        security.put("signature", "sig_123");
        payload.put("security", security);

        WebhookEvent event = handler.handle(payload);

        assertNotNull(event);
        assertInstanceOf(WebhookEvent.class, event);
    }

    @Test
    void testHandleInvalidJson() {
        assertThrows(InvalidPayloadException.class, () -> {
            handler.handle("invalid json");
        });
    }

    @Test
    void testHandleAsTransaction() throws Exception {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("id", "evt_123");
        payloadMap.put("type", "payment.completed");
        payloadMap.put("source", "payment");
        payloadMap.put("timestamp", "2024-01-01T00:00:00Z");

        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("status", "success");
        data.put("reference", "REF-123");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("channel", "mpesa");
        data.put("created_at", "2024-01-01T00:00:00Z");
        payloadMap.put("data", data);

        Map<String, Object> meta = new HashMap<>();
        meta.put("app_id", "app_123");
        payloadMap.put("meta", meta);

        Map<String, Object> security = new HashMap<>();
        security.put("signature", "sig_123");
        payloadMap.put("security", security);

        String payload = objectMapper.writeValueAsString(payloadMap);
        Transaction transaction = handler.handleAsTransaction(payload);

        assertNotNull(transaction);
        assertInstanceOf(Transaction.class, transaction);
        assertTrue(transaction.isSuccessful());
    }

    @Test
    void testHandleAsTransactionNonPaymentEvent() throws Exception {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("id", "evt_123");
        payloadMap.put("type", "subscription.created");
        payloadMap.put("source", "subscription");
        payloadMap.put("timestamp", "2024-01-01T00:00:00Z");

        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("status", "active");
        payloadMap.put("data", data);

        Map<String, Object> meta = new HashMap<>();
        meta.put("app_id", "app_123");
        payloadMap.put("meta", meta);

        Map<String, Object> security = new HashMap<>();
        security.put("signature", "sig_123");
        payloadMap.put("security", security);

        String payload = objectMapper.writeValueAsString(payloadMap);
        Transaction transaction = handler.handleAsTransaction(payload);

        assertNull(transaction);
    }
}

