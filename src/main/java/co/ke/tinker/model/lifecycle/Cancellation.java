package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Cancellation {
    @JsonProperty("id")
    public String id;
    @JsonProperty("payment_id")
    public String paymentId;
    @JsonProperty("status")
    public CancellationStatus status;
    @JsonProperty("reason")
    public String reason;
    @JsonProperty("reason_code")
    public String reasonCode;
    @JsonProperty("source")
    public String source;
    @JsonProperty("requested_by_app_id")
    public String requestedByAppId;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;
    @JsonProperty("completed_at")
    public String completedAt;
    @JsonProperty("failure_message")
    public String failureMessage;

    public Cancellation() {}

    public String getId() { return id; }

    public String getPaymentId() { return paymentId; }

    public CancellationStatus getStatus() { return status; }

    public String getReason() { return reason; }

    public String getReasonCode() { return reasonCode; }

    public String getSource() { return source; }

    public String getRequestedByAppId() { return requestedByAppId; }

    public String getCreatedAt() { return createdAt; }

    public String getUpdatedAt() { return updatedAt; }

    public String getCompletedAt() { return completedAt; }

    public String getFailureMessage() { return failureMessage; }
}
