package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WithdrawalFeeRule {
    @JsonProperty("fixed")
    public Double fixed;
    @JsonProperty("percent")
    public Double percent;
    @JsonProperty("min")
    public Double min;
    @JsonProperty("max")
    public Double max;

    public WithdrawalFeeRule() {}

    public Double getFixed() { return fixed; }

    public Double getPercent() { return percent; }

    public Double getMin() { return min; }

    public Double getMax() { return max; }
}
