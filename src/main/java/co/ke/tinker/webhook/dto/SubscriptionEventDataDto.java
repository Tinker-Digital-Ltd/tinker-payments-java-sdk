package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionEventDataDto {
    private final String id;
    private final String status;
    private final String planId;
    private final String accountId;
    private final String externalCustomerId;
    private final String currentPeriodStart;
    private final String currentPeriodEnd;
    private final String createdAt;
    private final String cancelledAt;
    private final String pausedAt;
    private final String reactivatedAt;
    private final String billingStatus;
    private final String trialEndsAt;
    private final String productId;
    private final List<String> entitlementIds;
    private final String store;
    private final String periodType;
    private final String expirationAt;
    private final String gracePeriodExpirationAt;
    private final String cancelReason;
    private final String expirationReason;
    private final String environment;
    private final String presentedOfferingId;
    private final String transactionId;
    private final String originalTransactionId;
    private final String previousProductId;
    private final String previousExpirationAt;

    public SubscriptionEventDataDto(Map<String, Object> data) {
        this.id = data.containsKey("subscription_id") ? (String) data.get("subscription_id") : (String) data.get("id");
        this.status = data.containsKey("status") ? (String) data.get("status") : "";
        this.planId = data.containsKey("plan_id") ? (String) data.get("plan_id") : "";
        this.accountId = data.containsKey("account_id") ? (String) data.get("account_id") : (data.containsKey("customer_id") ? (String) data.get("customer_id") : "");
        this.externalCustomerId = (String) data.get("external_customer_id");
        this.currentPeriodStart = data.containsKey("current_period_start") ? (String) data.get("current_period_start") : null;
        this.currentPeriodEnd = data.containsKey("current_period_end") ? (String) data.get("current_period_end") : null;
        this.createdAt = data.containsKey("created_at") ? (String) data.get("created_at") : (currentPeriodStart != null ? currentPeriodStart : "");
        this.cancelledAt = (String) data.get("cancelled_at");
        this.pausedAt = (String) data.get("paused_at");
        this.reactivatedAt = (String) data.get("reactivated_at");
        this.billingStatus = (String) data.get("billing_status");
        this.trialEndsAt = (String) data.get("trial_ends_at");
        this.productId = (String) data.get("product_id");
        this.entitlementIds = castList(data.get("entitlement_ids"));
        this.store = (String) data.get("store");
        this.periodType = (String) data.get("period_type");
        this.expirationAt = (String) data.get("expiration_at");
        this.gracePeriodExpirationAt = (String) data.get("grace_period_expiration_at");
        this.cancelReason = (String) data.get("cancel_reason");
        this.expirationReason = (String) data.get("expiration_reason");
        this.environment = (String) data.get("environment");
        this.presentedOfferingId = (String) data.get("presented_offering_id");
        this.transactionId = (String) data.get("transaction_id");
        this.originalTransactionId = (String) data.get("original_transaction_id");
        this.previousProductId = (String) data.get("previous_product_id");
        this.previousExpirationAt = (String) data.get("previous_expiration_at");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("subscription_id", id);
        result.put("status", status);
        result.put("plan_id", planId);
        result.put("account_id", accountId);
        result.put("external_customer_id", externalCustomerId);
        result.put("current_period_start", currentPeriodStart);
        result.put("current_period_end", currentPeriodEnd);
        result.put("created_at", createdAt);
        result.put("cancelled_at", cancelledAt);
        result.put("paused_at", pausedAt);
        result.put("reactivated_at", reactivatedAt);
        result.put("billing_status", billingStatus);
        result.put("trial_ends_at", trialEndsAt);
        result.put("product_id", productId);
        result.put("entitlement_ids", entitlementIds);
        result.put("store", store);
        result.put("period_type", periodType);
        result.put("expiration_at", expirationAt);
        result.put("grace_period_expiration_at", gracePeriodExpirationAt);
        result.put("cancel_reason", cancelReason);
        result.put("expiration_reason", expirationReason);
        result.put("environment", environment);
        result.put("presented_offering_id", presentedOfferingId);
        result.put("transaction_id", transactionId);
        result.put("original_transaction_id", originalTransactionId);
        result.put("previous_product_id", previousProductId);
        result.put("previous_expiration_at", previousExpirationAt);
        return result;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getPlanId() { return planId; }
    public String getAccountId() { return accountId; }
    public String getExternalCustomerId() { return externalCustomerId; }
    public String getBillingStatus() { return billingStatus; }
    public String getTrialEndsAt() { return trialEndsAt; }
    public String getProductId() { return productId; }
    public List<String> getEntitlementIds() { return entitlementIds; }
    public String getStore() { return store; }
    public String getPeriodType() { return periodType; }
    public String getExpirationAt() { return expirationAt; }
    public String getGracePeriodExpirationAt() { return gracePeriodExpirationAt; }
    public String getCancelReason() { return cancelReason; }
    public String getExpirationReason() { return expirationReason; }
    public String getEnvironment() { return environment; }
    public String getPresentedOfferingId() { return presentedOfferingId; }
    public String getTransactionId() { return transactionId; }
    public String getOriginalTransactionId() { return originalTransactionId; }
    public String getPreviousProductId() { return previousProductId; }
    public String getPreviousExpirationAt() { return previousExpirationAt; }

    @SuppressWarnings("unchecked")
    private static List<String> castList(Object value) {
        return value instanceof List ? (List<String>) value : List.of();
    }
}
