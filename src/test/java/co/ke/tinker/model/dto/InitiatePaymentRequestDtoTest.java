package co.ke.tinker.model.dto;

import co.ke.tinker.enums.Gateway;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InitiatePaymentRequestDtoTest {
    @Test
    void testSerializationExcludesNullOptionalFields() {
        InitiatePaymentRequestDto dto = new InitiatePaymentRequestDto(
            100.00,
            "KES",
            Gateway.MPESA,
            "ORDER-12345",
            "https://example.com/return"
        );

        Map<String, Object> map = dto.toMap();

        assertTrue(map.containsKey("amount"));
        assertTrue(map.containsKey("currency"));
        assertTrue(map.containsKey("gateway"));
        assertTrue(map.containsKey("merchantReference"));
        assertTrue(map.containsKey("returnUrl"));
        assertFalse(map.containsKey("customerPhone"));
        assertFalse(map.containsKey("customerEmail"));
        assertFalse(map.containsKey("transactionDesc"));
        assertFalse(map.containsKey("metadata"));
    }

    @Test
    void testSerializationIncludesOptionalFieldsWhenProvided() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("order_id", "12345");

        InitiatePaymentRequestDto dto = new InitiatePaymentRequestDto(
            100.00,
            "KES",
            Gateway.PAYSTACK,
            "ORDER-12345",
            "https://example.com/return",
            "+254712345678",
            "customer@example.com",
            "Payment for order",
            metadata
        );

        Map<String, Object> map = dto.toMap();

        assertTrue(map.containsKey("customerPhone"));
        assertTrue(map.containsKey("customerEmail"));
        assertTrue(map.containsKey("transactionDesc"));
        assertTrue(map.containsKey("metadata"));
    }
}

