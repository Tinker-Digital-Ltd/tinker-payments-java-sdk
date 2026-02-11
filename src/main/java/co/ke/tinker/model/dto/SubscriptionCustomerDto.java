package co.ke.tinker.model.dto;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionCustomerDto {
    private final String externalCustomerId;
    private final String name;
    private final String email;
    private final String phone;
    private final Map<String, Object> metadata;

    public SubscriptionCustomerDto(String externalCustomerId, String name) {
        this(externalCustomerId, name, null, null, null);
    }

    public SubscriptionCustomerDto(String externalCustomerId, String name, String email, String phone, Map<String, Object> metadata) {
        this.externalCustomerId = externalCustomerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.metadata = metadata;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("external_customer_id", externalCustomerId);
        payload.put("name", name);
        if (email != null) {
            payload.put("email", email);
        }
        if (phone != null) {
            payload.put("phone", phone);
        }
        if (metadata != null) {
            payload.put("metadata", metadata);
        }
        return payload;
    }
}
