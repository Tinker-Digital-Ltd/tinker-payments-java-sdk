package co.ke.tinker.model.dto;

import co.ke.tinker.enums.Gateway;

import java.util.HashMap;
import java.util.Map;

public class InitiatePaymentRequestDto {
    private final Double amount;
    private final String currency;
    private final Gateway gateway;
    private final String merchantReference;
    private final String returnUrl;
    private final String customerPhone;
    private final String customerEmail;
    private final String transactionDesc;
    private final Map<String, Object> metadata;

    public InitiatePaymentRequestDto(Double amount, String currency, Gateway gateway, String merchantReference, String returnUrl) {
        this(amount, currency, gateway, merchantReference, returnUrl, null, null, null, null);
    }

    public InitiatePaymentRequestDto(Double amount, String currency, Gateway gateway, String merchantReference, String returnUrl,
                                     String customerPhone, String customerEmail, String transactionDesc, Map<String, Object> metadata) {
        this.amount = amount;
        this.currency = currency;
        this.gateway = gateway;
        this.merchantReference = merchantReference;
        this.returnUrl = returnUrl;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.transactionDesc = transactionDesc;
        this.metadata = metadata;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amount);
        payload.put("currency", currency);
        payload.put("gateway", gateway.getValue());
        payload.put("merchantReference", merchantReference);
        payload.put("returnUrl", returnUrl);

        if (customerPhone != null) {
            payload.put("customerPhone", customerPhone);
        }
        if (customerEmail != null) {
            payload.put("customerEmail", customerEmail);
        }
        if (transactionDesc != null) {
            payload.put("transactionDesc", transactionDesc);
        }
        if (metadata != null) {
            payload.put("metadata", metadata);
        }

        return payload;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}

