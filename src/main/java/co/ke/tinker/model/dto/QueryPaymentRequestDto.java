package co.ke.tinker.model.dto;

import co.ke.tinker.enums.Gateway;

import java.util.HashMap;
import java.util.Map;

public class QueryPaymentRequestDto {
    private final String paymentReference;
    private final String gateway;

    public QueryPaymentRequestDto(String paymentReference, Gateway gateway) {
        this(paymentReference, gateway.getValue());
    }

    public QueryPaymentRequestDto(String paymentReference, String gateway) {
        this.paymentReference = paymentReference;
        this.gateway = gateway;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("reference", paymentReference);
        payload.put("gateway", gateway);
        return payload;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public String getGateway() {
        return gateway;
    }

    public String getGatewayId() {
        return gateway;
    }
}
