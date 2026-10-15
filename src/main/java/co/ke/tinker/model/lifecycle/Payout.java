package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Payout {
    @JsonProperty("id")
    public String id;
    @JsonProperty("account_id")
    public String accountId;
    @JsonProperty("payout_method_id")
    public String payoutMethodId;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("fee_amount")
    public Double feeAmount;
    @JsonProperty("net_amount")
    public Double netAmount;
    @JsonProperty("status")
    public PayoutStatus status;
    @JsonProperty("source")
    public String source;
    @JsonProperty("requested_by_app_id")
    public String requestedByAppId;
    @JsonProperty("requested_by_user_id")
    public String requestedByUserId;
    @JsonProperty("rejection_reason")
    public String rejectionReason;
    @JsonProperty("requested_at")
    public String requestedAt;
    @JsonProperty("approved_at")
    public String approvedAt;
    @JsonProperty("processing_at")
    public String processingAt;
    @JsonProperty("paid_at")
    public String paidAt;
    @JsonProperty("rejected_at")
    public String rejectedAt;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;

    public Payout() {}

    public String getId() { return id; }

    public String getAccountId() { return accountId; }

    public String getPayoutMethodId() { return payoutMethodId; }

    public Double getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public Double getFeeAmount() { return feeAmount; }

    public Double getNetAmount() { return netAmount; }

    public PayoutStatus getStatus() { return status; }

    public String getSource() { return source; }

    public String getRequestedByAppId() { return requestedByAppId; }

    public String getRequestedByUserId() { return requestedByUserId; }

    public String getRejectionReason() { return rejectionReason; }

    public String getRequestedAt() { return requestedAt; }

    public String getApprovedAt() { return approvedAt; }

    public String getProcessingAt() { return processingAt; }

    public String getPaidAt() { return paidAt; }

    public String getRejectedAt() { return rejectedAt; }

    public String getCreatedAt() { return createdAt; }

    public String getUpdatedAt() { return updatedAt; }
}
