package co.ke.tinker.model;

import co.ke.tinker.enums.PaymentStatus;
import co.ke.tinker.model.dto.CallbackDataDto;
import co.ke.tinker.model.dto.InitiationDataDto;
import co.ke.tinker.model.dto.QueryDataDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    void testInitializeWithInitiationData() {
        Map<String, Object> data = new HashMap<>();
        data.put("payment_reference", "TXN-123");
        data.put("status", "pending");
        data.put("authorization_url", "https://example.com/auth");

        Transaction transaction = new Transaction(data);

        assertNotNull(transaction.getInitiationData());
        assertInstanceOf(InitiationDataDto.class, transaction.getInitiationData());
        assertNull(transaction.getQueryData());
        assertNull(transaction.getCallbackData());
        assertEquals(PaymentStatus.PENDING, transaction.getStatus());
    }

    @Test
    void testInitializeWithQueryCallbackData() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("reference", "REF-123");
        data.put("status", "success");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("created_at", "2024-01-01T00:00:00Z");
        data.put("channel", "mpesa");

        Transaction transaction = new Transaction(data);

        assertNotNull(transaction.getQueryData());
        assertInstanceOf(QueryDataDto.class, transaction.getQueryData());
        assertNotNull(transaction.getCallbackData());
        assertInstanceOf(CallbackDataDto.class, transaction.getCallbackData());
        assertNull(transaction.getInitiationData());
        assertEquals(PaymentStatus.SUCCESS, transaction.getStatus());
    }

    @Test
    void testSuccessful() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("reference", "REF-123");
        data.put("status", "success");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("created_at", "2024-01-01T00:00:00Z");
        data.put("channel", "mpesa");

        Transaction transaction = new Transaction(data);
        assertTrue(transaction.isSuccessful());
        assertFalse(transaction.isPending());
        assertFalse(transaction.isCancelled());
        assertFalse(transaction.isFailed());
    }

    @Test
    void testPending() {
        Map<String, Object> data = new HashMap<>();
        data.put("payment_reference", "TXN-123");
        data.put("status", "pending");

        Transaction transaction = new Transaction(data);
        assertTrue(transaction.isPending());
        assertFalse(transaction.isSuccessful());
        assertFalse(transaction.isCancelled());
        assertFalse(transaction.isFailed());
    }

    @Test
    void testCancelled() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("reference", "REF-123");
        data.put("status", "cancelled");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("created_at", "2024-01-01T00:00:00Z");
        data.put("channel", "mpesa");

        Transaction transaction = new Transaction(data);
        assertTrue(transaction.isCancelled());
        assertFalse(transaction.isSuccessful());
        assertFalse(transaction.isPending());
        assertFalse(transaction.isFailed());
    }

    @Test
    void testFailed() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", "123");
        data.put("reference", "REF-123");
        data.put("status", "failed");
        data.put("amount", 100.0);
        data.put("currency", "KES");
        data.put("created_at", "2024-01-01T00:00:00Z");
        data.put("channel", "mpesa");

        Transaction transaction = new Transaction(data);
        assertTrue(transaction.isFailed());
        assertFalse(transaction.isSuccessful());
        assertFalse(transaction.isPending());
        assertFalse(transaction.isCancelled());
    }
}

