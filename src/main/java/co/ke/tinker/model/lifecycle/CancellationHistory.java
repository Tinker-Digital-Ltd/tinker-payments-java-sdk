package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CancellationHistory {
    @JsonProperty("cancellations")
    public java.util.List<Cancellation> cancellations;
    @JsonProperty("capability")
    public CancellationCapability capability;
    @JsonProperty("has_more")
    public boolean hasMore;

    public CancellationHistory() {}

    public java.util.List<Cancellation> getCancellations() { return cancellations; }

    public boolean getHasMore() { return hasMore; }

    public CancellationCapability getCapability() { return capability; }
}
