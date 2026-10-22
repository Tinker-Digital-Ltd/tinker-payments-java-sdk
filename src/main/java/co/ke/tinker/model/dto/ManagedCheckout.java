package co.ke.tinker.model.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/** Exact integer minor-unit prices for managed recurring billing. */
public final class ManagedCheckout {
    private final String externalCustomerId;
    private final String email;
    private final String name;
    private final String currency;
    private final long unitAmount;
    private final int quantity;
    private final String interval;
    private final String returnUrl;
    private final String idempotencyKey;
    private final int trialDays;
    private final Instant trialEnd;

    public ManagedCheckout(String externalCustomerId, String email, String name, String currency, long unitAmount, int quantity, String interval, String returnUrl, String idempotencyKey) {
        this(externalCustomerId, email, name, currency, unitAmount, quantity, interval, returnUrl, idempotencyKey, 0);
    }

    public ManagedCheckout(String externalCustomerId, String email, String name, String currency, long unitAmount, int quantity, String interval, String returnUrl, String idempotencyKey, int trialDays) {
        this(externalCustomerId, email, name, currency, unitAmount, quantity, interval, returnUrl, idempotencyKey, trialDays, null);
    }

    public ManagedCheckout(String externalCustomerId, String email, String name, String currency, long unitAmount, int quantity, String interval, String returnUrl, String idempotencyKey, int trialDays, Instant trialEnd) {
        this.externalCustomerId = externalCustomerId;
        this.email = email;
        this.name = name;
        this.currency = currency;
        this.unitAmount = unitAmount;
        this.quantity = quantity;
        this.interval = interval;
        this.returnUrl = returnUrl;
        this.idempotencyKey = idempotencyKey;
        this.trialDays = trialDays;
        this.trialEnd = trialEnd;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> body = new HashMap<>(Map.of(
                "external_customer_id", externalCustomerId,
                "email", email,
                "name", name,
                "currency", currency,
                "unit_amount", unitAmount,
                "quantity", quantity,
                "interval", interval,
                "return_url", returnUrl,
                "idempotency_key", idempotencyKey));
        if (trialDays > 0) {
            body.put("trial_days", trialDays);
        }
        if (trialEnd != null) {
            body.put("trial_end", trialEnd.getEpochSecond());
        }
        return body;
    }
}
