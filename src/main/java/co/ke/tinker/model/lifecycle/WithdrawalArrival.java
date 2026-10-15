package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WithdrawalArrival {
    @JsonProperty("min")
    public Integer min;
    @JsonProperty("max")
    public Integer max;
    @JsonProperty("unit")
    public String unit;

    public WithdrawalArrival() {}

    public Integer getMin() { return min; }

    public Integer getMax() { return max; }

    public String getUnit() { return unit; }
}
