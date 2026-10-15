package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Refund {
    @JsonProperty("id")
    public String id;
    @JsonProperty("payment_id")
    public String paymentId;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("reason")
    public RefundReason reason;
    @JsonProperty("note")
    public String note;
    @JsonProperty("source")
    public String source;
    @JsonProperty("requested_by_app_id")
    public String requestedByAppId;
    @JsonProperty("status")
    public RefundStatus status;
    @JsonProperty("execution")
    public String execution;
    @JsonProperty("provider_refund_id")
    public String providerRefundId;
    @JsonProperty("manual_reference")
    public String manualReference;
    @JsonProperty("rejection_reason")
    public String rejectionReason;
    @JsonProperty("failure_message")
    public String failureMessage;
    @JsonProperty("reviewed_at")
    public String reviewedAt;
    @JsonProperty("completed_at")
    public String completedAt;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;

    public Refund() {}

    public String getId() { return id; }

    public String getPaymentId() { return paymentId; }

    public Double getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public RefundReason getReason() { return reason; }

    public String getNote() { return note; }

    public String getSource() { return source; }

    public String getRequestedByAppId() { return requestedByAppId; }

    public RefundStatus getStatus() { return status; }

    public String getExecution() { return execution; }

    public String getProviderRefundId() { return providerRefundId; }

    public String getManualReference() { return manualReference; }

    public String getRejectionReason() { return rejectionReason; }

    public String getFailureMessage() { return failureMessage; }

    public String getReviewedAt() { return reviewedAt; }

    public String getCompletedAt() { return completedAt; }

    public String getCreatedAt() { return createdAt; }

    public String getUpdatedAt() { return updatedAt; }
}
