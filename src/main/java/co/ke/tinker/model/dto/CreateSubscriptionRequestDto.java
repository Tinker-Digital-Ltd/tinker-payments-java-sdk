package co.ke.tinker.model.dto;

import java.util.HashMap;
import java.util.Map;

public class CreateSubscriptionRequestDto {
    private final String planId;
    private final String gateway;
    private final SubscriptionCustomerDto customer;
    private final String paymentMethodId;
    private final String billingPeriod;

    public CreateSubscriptionRequestDto(String planId, String gateway, SubscriptionCustomerDto customer) {
        this(planId, gateway, customer, null, null);
    }

    public CreateSubscriptionRequestDto(String planId, String gateway, SubscriptionCustomerDto customer, String paymentMethodId, String billingPeriod) {
        this.planId = planId;
        this.gateway = gateway;
        this.customer = customer;
        this.paymentMethodId = paymentMethodId;
        this.billingPeriod = billingPeriod;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("plan_id", planId);
        payload.put("gateway", gateway);
        payload.put("customer", customer.toMap());
        if (paymentMethodId != null) {
            payload.put("payment_method_id", paymentMethodId);
        }
        if (billingPeriod != null) {
            payload.put("billing_period", billingPeriod);
        }
        return payload;
    }
}
