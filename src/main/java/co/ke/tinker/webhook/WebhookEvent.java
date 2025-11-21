package co.ke.tinker.webhook;

import co.ke.tinker.exception.InvalidPayloadException;
import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.model.Transaction;
import co.ke.tinker.webhook.dto.InvoiceEventDataDto;
import co.ke.tinker.webhook.dto.PaymentEventDataDto;
import co.ke.tinker.webhook.dto.SettlementEventDataDto;
import co.ke.tinker.webhook.dto.SubscriptionEventDataDto;

import java.util.Map;

public class WebhookEvent {
    private final String id;
    private final String type;
    private final String source;
    private final String timestamp;
    private final Object data;
    private final WebhookMeta meta;
    private final WebhookSecurity security;

    @SuppressWarnings("unchecked")
    public WebhookEvent(Map<String, Object> payload) {
        this.id = (String) payload.get("id");
        this.type = (String) payload.get("type");
        this.source = (String) payload.get("source");
        this.timestamp = (String) payload.get("timestamp");
        this.data = createEventData((Map<String, Object>) payload.get("data"), this.source);
        this.meta = new WebhookMeta((Map<String, Object>) payload.get("meta"));
        this.security = new WebhookSecurity((Map<String, Object>) payload.get("security"));
    }

    public boolean isPaymentEvent() {
        return "payment".equals(source);
    }

    public boolean isSubscriptionEvent() {
        return "subscription".equals(source);
    }

    public boolean isInvoiceEvent() {
        return "invoice".equals(source);
    }

    public boolean isSettlementEvent() {
        return "settlement".equals(source);
    }

    public PaymentEventDataDto getPaymentData() {
        return data instanceof PaymentEventDataDto ? (PaymentEventDataDto) data : null;
    }

    public SubscriptionEventDataDto getSubscriptionData() {
        return data instanceof SubscriptionEventDataDto ? (SubscriptionEventDataDto) data : null;
    }

    public InvoiceEventDataDto getInvoiceData() {
        return data instanceof InvoiceEventDataDto ? (InvoiceEventDataDto) data : null;
    }

    public SettlementEventDataDto getSettlementData() {
        return data instanceof SettlementEventDataDto ? (SettlementEventDataDto) data : null;
    }

    public Transaction toTransaction() {
        if (!isPaymentEvent()) {
            return null;
        }
        if (!(data instanceof PaymentEventDataDto)) {
            return null;
        }
        PaymentEventDataDto paymentData = (PaymentEventDataDto) data;
        return new Transaction(paymentData.toMap());
    }

    private Object createEventData(Map<String, Object> data, String source) {
        if (data == null) {
            return null;
        }
        switch (source) {
            case "payment":
                return new PaymentEventDataDto(data);
            case "subscription":
                return new SubscriptionEventDataDto(data);
            case "invoice":
                return new InvoiceEventDataDto(data);
            case "settlement":
                return new SettlementEventDataDto(data);
            default:
                throw new InvalidPayloadException("Unknown webhook source: " + source, ExceptionCode.INVALID_PAYLOAD);
        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }

    public WebhookMeta getMeta() {
        return meta;
    }

    public WebhookSecurity getSecurity() {
        return security;
    }
}

