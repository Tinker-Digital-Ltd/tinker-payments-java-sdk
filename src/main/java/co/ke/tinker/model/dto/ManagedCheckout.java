package co.ke.tinker.model.dto;

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

    public ManagedCheckout(String externalCustomerId, String email, String name, String currency, long unitAmount, int quantity, String interval, String returnUrl, String idempotencyKey) {
        this.externalCustomerId = externalCustomerId;
        this.email = email;
        this.name = name;
        this.currency = currency;
        this.unitAmount = unitAmount;
        this.quantity = quantity;
        this.interval = interval;
        this.returnUrl = returnUrl;
        this.idempotencyKey = idempotencyKey;
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "external_customer_id", externalCustomerId,
                "email", email,
                "name", name,
                "currency", currency,
                "unit_amount", unitAmount,
                "quantity", quantity,
                "interval", interval,
                "return_url", returnUrl,
                "idempotency_key", idempotencyKey);
    }
}
