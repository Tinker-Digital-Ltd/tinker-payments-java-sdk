package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CancellationCapability {
    @JsonProperty("supported")
    public Boolean supported;
    @JsonProperty("eligible")
    public Boolean eligible;
    @JsonProperty("reason_code")
    public String reasonCode;

    public CancellationCapability() {}

    public Boolean getSupported() { return supported; }

    public Boolean getEligible() { return eligible; }

    public String getReasonCode() { return reasonCode; }
}
