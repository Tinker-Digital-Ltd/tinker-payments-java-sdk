package co.ke.tinker.model.lifecycle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Currency-unit lifecycle contract. Acceptance is separate from completion. */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PayoutFilters {
    @JsonProperty("page")
    public Integer page;
    @JsonProperty("per_page")
    public Integer perPage;
    @JsonProperty("status")
    public PayoutStatus status;
    @JsonProperty("currency")
    public String currency;

    public PayoutFilters() {}

    public Integer getPage() { return page; }

    public Integer getPerPage() { return perPage; }

    public PayoutStatus getStatus() { return status; }

    public String getCurrency() { return currency; }
}
