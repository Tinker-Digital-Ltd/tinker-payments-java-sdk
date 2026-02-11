package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.Map;

public class InvoiceEventDataDto {
    private final String id;
    private final String status;
    private final String invoiceNumber;
    private final Double amount;
    private final String currency;
    private final String subscriptionId;
    private final String createdAt;
    private final String paidAt;

    public InvoiceEventDataDto(Map<String, Object> data) {
        this.id = data.containsKey("invoice_id") ? (String) data.get("invoice_id") : (String) data.get("id");
        this.status = data.containsKey("status") ? (String) data.get("status") : "";
        this.invoiceNumber = data.containsKey("invoice_number") ? (String) data.get("invoice_number") : "";
        Object amountObj = data.get("amount");
        this.amount = amountObj != null ? ((Number) amountObj).doubleValue() : 0.0;
        this.currency = data.containsKey("currency") ? (String) data.get("currency") : "";
        this.subscriptionId = data.containsKey("subscription_id") ? (String) data.get("subscription_id") : "";
        this.createdAt = data.containsKey("created_at") ? (String) data.get("created_at") : "";
        this.paidAt = (String) data.get("paid_at");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("invoice_id", id);
        result.put("status", status);
        result.put("invoice_number", invoiceNumber);
        result.put("amount", amount);
        result.put("currency", currency);
        result.put("subscription_id", subscriptionId);
        result.put("created_at", createdAt);
        result.put("paid_at", paidAt);
        return result;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public Double getAmount() { return amount; }
}
