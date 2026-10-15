package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PaymentDetail {
    @JsonProperty("id")
    public String id;
    @JsonProperty("status")
    public String status;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("refundable_amount")
    public Double refundableAmount;
    @JsonProperty("refund_capability")
    public RefundCapability refundCapability;
    @JsonProperty("refunds")
    public java.util.List<Refund> refunds;

    public PaymentDetail() {}

    public String getId() { return id; }

    public String getStatus() { return status; }

    public Double getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public Double getRefundableAmount() { return refundableAmount; }

    public RefundCapability getRefundCapability() { return refundCapability; }

    public java.util.List<Refund> getRefunds() { return refunds; }
}
