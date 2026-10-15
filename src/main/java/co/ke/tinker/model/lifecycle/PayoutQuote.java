package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutQuote {
    @JsonProperty("method_type")
    public String methodType;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("amount")
    public Double amount;
    @JsonProperty("fee_amount")
    public Double feeAmount;
    @JsonProperty("net_amount")
    public Double netAmount;
    @JsonProperty("fee")
    public WithdrawalFeeRule fee;
    @JsonProperty("review_time")
    public String reviewTime;
    @JsonProperty("arrival")
    public WithdrawalArrival arrival;
    @JsonProperty("arrival_text")
    public String arrivalText;
    @JsonProperty("configured")
    public Boolean configured;
    @JsonProperty("is_estimate")
    public Boolean isEstimate;

    public PayoutQuote() {}

    public String getMethodType() { return methodType; }

    public String getCurrency() { return currency; }

    public Double getAmount() { return amount; }

    public Double getFeeAmount() { return feeAmount; }

    public Double getNetAmount() { return netAmount; }

    public WithdrawalFeeRule getFee() { return fee; }

    public String getReviewTime() { return reviewTime; }

    public WithdrawalArrival getArrival() { return arrival; }

    public String getArrivalText() { return arrivalText; }

    public Boolean getConfigured() { return configured; }

    public Boolean getIsEstimate() { return isEstimate; }
}
