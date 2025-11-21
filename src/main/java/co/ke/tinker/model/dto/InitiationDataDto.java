package co.ke.tinker.model.dto;

import co.ke.tinker.enums.PaymentStatus;

import java.util.HashMap;
import java.util.Map;

public class InitiationDataDto {
    private final String paymentReference;
    private final PaymentStatus status;
    private final String authorizationUrl;

    public InitiationDataDto(Map<String, Object> data) {
        this.paymentReference = (String) data.get("payment_reference");
        String statusValue = data.containsKey("status") ? (String) data.get("status") : "pending";
        this.status = PaymentStatus.fromString(statusValue);
        this.authorizationUrl = (String) data.get("authorization_url");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("payment_reference", paymentReference);
        result.put("status", status.getValue());
        result.put("authorization_url", authorizationUrl);
        return result;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
}

