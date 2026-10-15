package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RefundCapability {
    @JsonProperty("supported")
    public Boolean supported;
    @JsonProperty("partial")
    public Boolean partial;
    @JsonProperty("reasons")
    public java.util.List<String> reasons;
    @JsonProperty("max_amount")
    public Double maxAmount;
    @JsonProperty("review_required")
    public Boolean reviewRequired;
    @JsonProperty("execution")
    public String execution;
    @JsonProperty("unavailable_reason")
    public String unavailableReason;

    public RefundCapability() {}

    public Boolean getSupported() { return supported; }

    public Boolean getPartial() { return partial; }

    public java.util.List<String> getReasons() { return reasons; }

    public Double getMaxAmount() { return maxAmount; }

    public Boolean getReviewRequired() { return reviewRequired; }

    public String getExecution() { return execution; }

    public String getUnavailableReason() { return unavailableReason; }
}
