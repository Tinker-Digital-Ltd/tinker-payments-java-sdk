package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutMethod {
    @JsonProperty("id")
    public String id;
    @JsonProperty("method_type")
    public String methodType;
    @JsonProperty("provider")
    public String provider;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("status")
    public String status;
    @JsonProperty("is_verified")
    public Boolean isVerified;
    @JsonProperty("is_default")
    public Boolean isDefault;
    @JsonProperty("masked_destination")
    public String maskedDestination;

    public PayoutMethod() {}

    public String getId() { return id; }

    public String getMethodType() { return methodType; }

    public String getProvider() { return provider; }

    public String getCurrency() { return currency; }

    public String getStatus() { return status; }

    public Boolean getIsVerified() { return isVerified; }

    public Boolean getIsDefault() { return isDefault; }

    public String getMaskedDestination() { return maskedDestination; }
}
