package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CancellationResult {
    @JsonProperty("cancellation")
    public Cancellation cancellation;
    @JsonProperty("replayed")
    public Boolean replayed;

    public CancellationResult() {}

    public Cancellation getCancellation() { return cancellation; }

    public Boolean getReplayed() { return replayed; }
}
