package co.ke.tinker.model.dto;

import co.ke.tinker.enums.PaymentStatus;

import java.util.HashMap;
import java.util.Map;

public class InitiationDataDto {
    private final String paymentReference;
    private final PaymentStatus status;
    private final String statusValue;
    private final String authorizationUrl;
    private final String clientSecret;
    private final String publishableKey;

    public InitiationDataDto(Map<String, Object> data) {
        this.paymentReference = data.containsKey("paymentReference")
                ? (String) data.get("paymentReference")
                : (String) data.get("payment_reference");
        this.statusValue = PaymentStatus.normalize((String) data.get("status"));
        this.status = PaymentStatus.fromString(statusValue);
        this.authorizationUrl = data.containsKey("authorizationUrl")
                ? (String) data.get("authorizationUrl")
                : (String) data.get("authorization_url");
        this.clientSecret = data.containsKey("clientSecret")
                ? (String) data.get("clientSecret")
                : (String) data.get("client_secret");
        this.publishableKey = data.containsKey("publishableKey")
                ? (String) data.get("publishableKey")
                : (String) data.get("publishable_key");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentReference", paymentReference);
        result.put("status", statusValue);
        result.put("authorizationUrl", authorizationUrl);
        result.put("clientSecret", clientSecret);
        result.put("publishableKey", publishableKey);
        return result;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getPublishableKey() {
        return publishableKey;
    }
}
