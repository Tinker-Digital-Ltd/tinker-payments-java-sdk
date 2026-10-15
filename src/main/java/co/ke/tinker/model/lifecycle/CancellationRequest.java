package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CancellationRequest {
    @JsonProperty("reason")
    public String reason;

    public CancellationRequest() {}

    public CancellationRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() { return reason; }
}
