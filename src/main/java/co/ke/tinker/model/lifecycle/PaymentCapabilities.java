package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class PaymentCapabilities {
    @JsonProperty("providers")
    public List<ProviderCapability> providers;

    public PaymentCapabilities() {}

    public List<ProviderCapability> getProviders() { return providers; }
}
