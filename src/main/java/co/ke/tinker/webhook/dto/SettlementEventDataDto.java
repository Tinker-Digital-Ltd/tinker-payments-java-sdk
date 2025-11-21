package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.Map;

public class SettlementEventDataDto {
    private final String id;
    private final String status;
    private final Double amount;
    private final String currency;
    private final String settlementDate;
    private final String createdAt;
    private final String processedAt;

    public SettlementEventDataDto(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.status = (String) data.get("status");
        Object amountObj = data.get("amount");
        this.amount = amountObj != null ? ((Number) amountObj).doubleValue() : null;
        this.currency = (String) data.get("currency");
        this.settlementDate = data.containsKey("settlement_date") ? (String) data.get("settlement_date") : "";
        this.createdAt = data.containsKey("created_at") ? (String) data.get("created_at") : "";
        this.processedAt = (String) data.get("processed_at");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("status", status);
        result.put("amount", amount);
        result.put("currency", currency);
        result.put("settlement_date", settlementDate);
        result.put("created_at", createdAt);
        result.put("processed_at", processedAt);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getProcessedAt() {
        return processedAt;
    }
}

