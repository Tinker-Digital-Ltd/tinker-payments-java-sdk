package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionEventDataDto {
    private final String id;
    private final String status;
    private final String planId;
    private final String customerId;
    private final String createdAt;
    private final String cancelledAt;
    private final String pausedAt;
    private final String reactivatedAt;

    public SubscriptionEventDataDto(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.status = (String) data.get("status");
        this.planId = data.containsKey("plan_id") ? (String) data.get("plan_id") : "";
        this.customerId = data.containsKey("customer_id") ? (String) data.get("customer_id") : "";
        this.createdAt = data.containsKey("created_at") ? (String) data.get("created_at") : "";
        this.cancelledAt = (String) data.get("cancelled_at");
        this.pausedAt = (String) data.get("paused_at");
        this.reactivatedAt = (String) data.get("reactivated_at");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("status", status);
        result.put("plan_id", planId);
        result.put("customer_id", customerId);
        result.put("created_at", createdAt);
        result.put("cancelled_at", cancelledAt);
        result.put("paused_at", pausedAt);
        result.put("reactivated_at", reactivatedAt);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getPlanId() {
        return planId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public String getPausedAt() {
        return pausedAt;
    }

    public String getReactivatedAt() {
        return reactivatedAt;
    }
}

