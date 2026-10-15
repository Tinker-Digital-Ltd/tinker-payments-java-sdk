package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutResult {
    @JsonProperty("withdrawal")
    public Payout withdrawal;
    @JsonProperty("replayed")
    public Boolean replayed;

    public PayoutResult() {}

    public Payout getWithdrawal() { return withdrawal; }

    public Boolean getReplayed() { return replayed; }
}
