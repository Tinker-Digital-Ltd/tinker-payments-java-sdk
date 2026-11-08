package co.ke.tinker.model;

import co.ke.tinker.enums.PaymentStatus;
import co.ke.tinker.model.dto.CallbackDataDto;
import co.ke.tinker.model.dto.InitiationDataDto;
import co.ke.tinker.model.dto.QueryDataDto;

import java.util.Map;

public class Transaction {
    private final PaymentStatus status;
    private final String statusValue;
    private final InitiationDataDto initiationData;
    private final QueryDataDto queryData;
    private final CallbackDataDto callbackData;

    public Transaction(Map<String, Object> data) {
        if ((data.containsKey("paymentReference") || data.containsKey("payment_reference")) && !data.containsKey("id")) {
            this.initiationData = new InitiationDataDto(data);
            this.queryData = null;
            this.callbackData = null;
            this.status = this.initiationData.getStatus();
            this.statusValue = this.initiationData.getStatusValue();
        } else if (data.containsKey("id") && data.containsKey("reference")) {
            this.initiationData = null;
            this.queryData = new QueryDataDto(data);
            this.callbackData = new CallbackDataDto(data);
            this.status = this.queryData.getStatus();
            this.statusValue = this.queryData.getStatusValue();
        } else {
            this.initiationData = null;
            this.queryData = null;
            this.callbackData = null;
            this.statusValue = PaymentStatus.normalize((String) data.get("status"));
            this.status = PaymentStatus.fromString(statusValue);
        }
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public InitiationDataDto getInitiationData() {
        return initiationData;
    }

    public QueryDataDto getQueryData() {
        return queryData;
    }

    public CallbackDataDto getCallbackData() {
        return callbackData;
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }

    public boolean isCancelled() {
        return status == PaymentStatus.CANCELLED;
    }

    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }
}
