package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutQuoteRequest {
    @JsonProperty("payout_method_id")
    public String payoutMethodId;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("currency")
    public String currency;

    public PayoutQuoteRequest() {}

    public PayoutQuoteRequest(String payoutMethodId, Double amount) {
        this.payoutMethodId = payoutMethodId;
        this.amount = amount;
    }

    public String getPayoutMethodId() { return payoutMethodId; }

    public Double getAmount() { return amount; }

    public String getCurrency() { return currency; }
}
