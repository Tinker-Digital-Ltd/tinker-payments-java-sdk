package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RefundRequest {
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("reason")
    public RefundReason reason;
    @JsonProperty("note")
    public String note;

    public RefundRequest() {}

    public RefundRequest(RefundReason reason) {
        this.reason = reason;
    }

    public Double getAmount() { return amount; }

    public RefundReason getReason() { return reason; }

    public String getNote() { return note; }
}
