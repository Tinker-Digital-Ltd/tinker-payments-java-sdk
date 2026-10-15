package co.ke.tinker.api;

import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.model.lifecycle.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Merchant wallet withdrawals to its own verified destinations. */
public final class PayoutManager extends BaseManager {
    public PayoutManager(Configuration config, HttpClient httpClient, AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }
    public List<PayoutMethod> methods() {
        Map<String, Object> response = request("GET", "merchant/payout-methods", null);
        List<PayoutMethod> result = new ArrayList<>();
        Object value = response.get("value");
        if (!(value instanceof List)) {
            throw new InvalidPayloadException("Payout methods response must contain a list");
        }
        for (Object item : (List<?>) value) {
            result.add(LifecycleCodec.decode(LifecycleCodec.payload(item), PayoutMethod.class));
        }
        return result;
    }
    public PayoutQuote quote(PayoutQuoteRequest payload) {
        LifecycleCodec.positiveAmount(payload.amount);
        return LifecycleCodec.decode(request("GET", "merchant/payouts/quote" + LifecycleCodec.query(payload), null), PayoutQuote.class);
    }
    public PayoutResult requestPayout(PayoutRequest payload, String key) {
        LifecycleCodec.positiveAmount(payload.amount);
        if (payload.expectedFeeAmount == null || !Double.isFinite(payload.expectedFeeAmount) || payload.expectedFeeAmount < 0) {
            throw new IllegalArgumentException("expected_fee_amount must be finite and non-negative");
        }
        return LifecycleCodec.decode(request("POST", "merchant/payouts", LifecycleCodec.payload(payload), LifecycleCodec.headers(key)), PayoutResult.class);
    }
    public PayoutPage list(PayoutFilters filters) {
        return LifecycleCodec.decode(request("GET", "merchant/payouts" + (filters == null ? "" : LifecycleCodec.query(filters)), null), PayoutPage.class);
    }
    public Payout get(String id) {
        return LifecycleCodec.decode(request("GET", "merchant/payouts/" + LifecycleCodec.encode(id), null), Payout.class);
    }
}
