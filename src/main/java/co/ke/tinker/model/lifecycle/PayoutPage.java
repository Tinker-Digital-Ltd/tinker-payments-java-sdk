package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutPage {
    @JsonProperty("payouts")
    public java.util.List<Payout> payouts;
    @JsonProperty("pagination")
    public Pagination pagination;

    public PayoutPage() {}

    public java.util.List<Payout> getPayouts() { return payouts; }

    public Pagination getPagination() { return pagination; }
}
