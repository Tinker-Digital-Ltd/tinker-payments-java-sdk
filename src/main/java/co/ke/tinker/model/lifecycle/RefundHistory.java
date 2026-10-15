package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RefundHistory {
    @JsonProperty("refunded_amount")
    public Double refundedAmount;
    @JsonProperty("refundable_amount")
    public Double refundableAmount;
    @JsonProperty("refund_capability")
    public RefundCapability refundCapability;
    @JsonProperty("refunds")
    public java.util.List<Refund> refunds;

    public RefundHistory() {}

    public Double getRefundedAmount() { return refundedAmount; }

    public Double getRefundableAmount() { return refundableAmount; }

    public RefundCapability getRefundCapability() { return refundCapability; }

    public java.util.List<Refund> getRefunds() { return refunds; }
}
