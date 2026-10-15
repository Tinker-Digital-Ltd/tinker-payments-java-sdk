package co.ke.tinker.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

final class LifecycleCodec {
    private static final ObjectMapper MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private LifecycleCodec() {}

    static Map<String, Object> payload(Object value) {
        return MAPPER.convertValue(value, new TypeReference<Map<String, Object>>() {});
    }

    static <T> T decode(Map<String, Object> value, Class<T> type) {
        return MAPPER.convertValue(value, type);
    }

    static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20"); }
    static String payment(String id) { return "merchant/payment/" + encode(id); }
    static Map<String, String> headers(String key) {
        String normalized = key == null ? "" : key.strip();
        if (normalized.isEmpty() || normalized.getBytes(StandardCharsets.UTF_8).length > 255) {
            throw new IllegalArgumentException("Idempotency-Key must contain 1 to 255 UTF-8 bytes");
        }
        return Map.of("Idempotency-Key", normalized);
    }
    static void positiveAmount(Double amount) {
        if (amount == null || !Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be finite and positive");
        }
    }
    static String query(Object filters) {
        String query = payload(filters).entrySet().stream()
            .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
        return query.isEmpty() ? "" : "?" + query;
    }
}
