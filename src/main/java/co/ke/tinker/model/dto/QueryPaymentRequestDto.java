package co.ke.tinker.model.dto;

import co.ke.tinker.enums.Gateway;

import java.util.HashMap;
import java.util.Map;

public class QueryPaymentRequestDto {
    private final String paymentReference;
    private final Gateway gateway;

    public QueryPaymentRequestDto(String paymentReference, Gateway gateway) {
        this.paymentReference = paymentReference;
        this.gateway = gateway;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("payment_reference", paymentReference);
        payload.put("gateway", gateway.getValue());
        return payload;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public Gateway getGateway() {
        return gateway;
    }
}

