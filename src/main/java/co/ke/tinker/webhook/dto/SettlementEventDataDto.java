package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.Map;

public class SettlementEventDataDto {
    private final String id;
    private final String status;
    private final Double amount;
    private final Double netAmount;
    private final String currency;
    private final String settlementDate;
    private final String createdAt;
    private final String processedAt;

    public SettlementEventDataDto(Map<String, Object> data) {
        this.id = data.containsKey("settlement_id") ? (String) data.get("settlement_id") : (String) data.get("id");
        this.status = data.containsKey("status") ? (String) data.get("status") : "";
        Object amountObj = data.get("amount");
        this.amount = amountObj != null ? ((Number) amountObj).doubleValue() : 0.0;
        Object netAmountObj = data.get("net_amount");
        this.netAmount = netAmountObj != null ? ((Number) netAmountObj).doubleValue() : null;
        this.currency = data.containsKey("currency") ? (String) data.get("currency") : "";
        this.settlementDate = data.containsKey("settlement_date") ? (String) data.get("settlement_date") : "";
        this.createdAt = data.containsKey("created_at") ? (String) data.get("created_at") : "";
        this.processedAt = (String) data.get("processed_at");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("settlement_id", id);
        result.put("status", status);
        result.put("amount", amount);
        result.put("net_amount", netAmount);
        result.put("currency", currency);
        result.put("settlement_date", settlementDate);
        result.put("created_at", createdAt);
        result.put("processed_at", processedAt);
        return result;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public Double getAmount() { return amount; }
}
