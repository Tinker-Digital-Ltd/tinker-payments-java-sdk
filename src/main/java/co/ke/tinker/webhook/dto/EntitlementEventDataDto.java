package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntitlementEventDataDto {
    private final String id;
    private final String transactionId;
    private final String productId;
    private final String externalCustomerId;
    private final List<String> entitlementIds;
    private final String store;
    private final String periodType;
    private final Double amount;
    private final String currency;
    private final String purchasedAt;
    private final String expirationAt;
    private final String presentedOfferingId;
    private final String revokedAt;
    private final String environment;
    private final String originalTransactionId;

    public EntitlementEventDataDto(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.transactionId = (String) data.get("transaction_id");
        this.productId = (String) data.get("product_id");
        this.externalCustomerId = (String) data.get("external_customer_id");
        this.entitlementIds = data.get("entitlement_ids") instanceof List ? castList(data.get("entitlement_ids")) : List.of();
        this.store = (String) data.get("store");
        this.periodType = (String) data.get("period_type");
        this.amount = data.get("amount") instanceof Number ? ((Number) data.get("amount")).doubleValue() : null;
        this.currency = (String) data.get("currency");
        this.purchasedAt = (String) data.get("purchased_at");
        this.expirationAt = (String) data.get("expiration_at");
        this.presentedOfferingId = (String) data.get("presented_offering_id");
        this.revokedAt = (String) data.get("revoked_at");
        this.environment = (String) data.get("environment");
        this.originalTransactionId = (String) data.get("original_transaction_id");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("transaction_id", transactionId);
        result.put("product_id", productId);
        result.put("external_customer_id", externalCustomerId);
        result.put("entitlement_ids", entitlementIds);
        result.put("store", store);
        result.put("period_type", periodType);
        result.put("amount", amount);
        result.put("currency", currency);
        result.put("purchased_at", purchasedAt);
        result.put("expiration_at", expirationAt);
        result.put("presented_offering_id", presentedOfferingId);
        result.put("revoked_at", revokedAt);
        result.put("environment", environment);
        result.put("original_transaction_id", originalTransactionId);
        return result;
    }

    public String getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getProductId() { return productId; }
    public String getExternalCustomerId() { return externalCustomerId; }
    public List<String> getEntitlementIds() { return entitlementIds; }
    public String getStore() { return store; }
    public String getPeriodType() { return periodType; }
    public Double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPurchasedAt() { return purchasedAt; }
    public String getExpirationAt() { return expirationAt; }
    public String getPresentedOfferingId() { return presentedOfferingId; }
    public String getRevokedAt() { return revokedAt; }
    public String getEnvironment() { return environment; }
    public String getOriginalTransactionId() { return originalTransactionId; }

    @SuppressWarnings("unchecked")
    private static List<String> castList(Object value) {
        return (List<String>) value;
    }
}
