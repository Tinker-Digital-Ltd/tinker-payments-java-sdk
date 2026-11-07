package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ProviderCapability {
    @JsonProperty("gateway")
    public String gateway;
    @JsonProperty("name")
    public String name;
    @JsonProperty("methods")
    public List<String> methods;
    @JsonProperty("currencies")
    public List<String> currencies;
    @JsonProperty("countries")
    public List<String> countries;
    @JsonProperty("capabilities")
    public Map<String, String> capabilities;

    public ProviderCapability() {}

    public String getGateway() { return gateway; }

    public String getName() { return name; }

    public List<String> getMethods() { return methods; }

    public List<String> getCurrencies() { return currencies; }

    public List<String> getCountries() { return countries; }

    public Map<String, String> getCapabilities() { return capabilities; }
}
