package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RefundResult {
    @JsonProperty("refund")
    public Refund refund;
    @JsonProperty("payment")
    public PaymentDetail payment;
    @JsonProperty("replayed")
    public Boolean replayed;

    public RefundResult() {}

    public Refund getRefund() { return refund; }

    public PaymentDetail getPayment() { return payment; }

    public Boolean getReplayed() { return replayed; }
}
