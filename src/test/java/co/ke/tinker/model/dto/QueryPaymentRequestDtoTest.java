package co.ke.tinker.model.dto;

import co.ke.tinker.enums.Gateway;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryPaymentRequestDtoTest {
    @Test
    void testSerialization() {
        QueryPaymentRequestDto dto = new QueryPaymentRequestDto(
            "TXN-abc123xyz",
            Gateway.MPESA
        );

        Map<String, Object> map = dto.toMap();

        assertEquals("TXN-abc123xyz", map.get("reference"));
        assertEquals("mpesa", map.get("gateway"));
        assertEquals(2, map.size());
    }
}
