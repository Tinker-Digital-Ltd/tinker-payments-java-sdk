package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutRequest {
    @JsonProperty("payout_method_id")
    public String payoutMethodId;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("expected_fee_amount")
    public Double expectedFeeAmount;

    public PayoutRequest() {}

    public PayoutRequest(String payoutMethodId, Double amount, String currency, Double expectedFeeAmount) {
        this.payoutMethodId = payoutMethodId;
        this.amount = amount;
        this.currency = currency;
        this.expectedFeeAmount = expectedFeeAmount;
    }

    public String getPayoutMethodId() { return payoutMethodId; }

    public Double getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public Double getExpectedFeeAmount() { return expectedFeeAmount; }
}
