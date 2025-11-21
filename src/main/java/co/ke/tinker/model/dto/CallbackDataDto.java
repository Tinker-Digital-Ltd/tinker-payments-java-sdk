package co.ke.tinker.model.dto;

import co.ke.tinker.enums.PaymentStatus;

import java.util.HashMap;
import java.util.Map;

public class CallbackDataDto {
    private final String id;
    private final PaymentStatus status;
    private final String reference;
    private final Double amount;
    private final String currency;
    private final String paidAt;
    private final String createdAt;
    private final String channel;

    public CallbackDataDto(Map<String, Object> data) {
        this.id = (String) data.get("id");
        String statusValue = data.containsKey("status") ? (String) data.get("status") : "pending";
        this.status = PaymentStatus.fromString(statusValue);
        this.reference = (String) data.get("reference");
        Object amountObj = data.get("amount");
        this.amount = amountObj != null ? ((Number) amountObj).doubleValue() : null;
        this.currency = (String) data.get("currency");
        this.paidAt = (String) data.get("paid_at");
        this.createdAt = (String) data.get("created_at");
        this.channel = (String) data.get("channel");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("status", status.getValue());
        result.put("reference", reference);
        result.put("amount", amount);
        result.put("currency", currency);
        result.put("paid_at", paidAt);
        result.put("created_at", createdAt);
        result.put("channel", channel);
        return result;
    }

    public String getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getReference() {
        return reference;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getChannel() {
        return channel;
    }
}

