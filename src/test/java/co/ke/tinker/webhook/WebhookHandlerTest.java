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
    @Test
    void verifiesServerSignatureAndRejectsTampering() throws Exception {
        String unsigned = "{\"id\":\"event-1\",\"type\":\"payment.completed\",\"source\":\"payment\",\"timestamp\":\"2026-09-06T00:00:00Z\",\"data\":{\"amount\":15,\"currency\":\"EUR\"},\"meta\":{\"app_id\":\"app-1\"}}";
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        mac.init(new javax.crypto.spec.SecretKeySpec("test-secret".getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256"));
        StringBuilder signature = new StringBuilder("sha256=");
        for (byte value : mac.doFinal(unsigned.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
            signature.append(String.format("%02x", value));
        }
        String signed = unsigned.substring(0, unsigned.length() - 1) + ",\"security\":{\"signature\":\"" + signature + "\"}}";
        assertTrue(handler.verifySignature(signed, "test-secret"));
        assertFalse(handler.verifySignature(signed, "wrong-secret"));
        assertFalse(handler.verifySignature(signed, ""));
        assertFalse(handler.verifySignature(signed.replace("15", "150"), "test-secret"));
        assertFalse(handler.verifySignature(signed.replace(signature.toString(), "sha256=1"), "test-secret"));
    }
}
