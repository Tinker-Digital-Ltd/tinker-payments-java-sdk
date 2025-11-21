package co.ke.tinker.model;

import co.ke.tinker.enums.PaymentStatus;
import co.ke.tinker.model.dto.CallbackDataDto;
import co.ke.tinker.model.dto.InitiationDataDto;
import co.ke.tinker.model.dto.QueryDataDto;

import java.util.Map;

public class Transaction {
    private final PaymentStatus status;
    private final InitiationDataDto initiationData;
    private final QueryDataDto queryData;
    private final CallbackDataDto callbackData;

    public Transaction(Map<String, Object> data) {
        if (data.containsKey("payment_reference") && !data.containsKey("id")) {
            this.initiationData = new InitiationDataDto(data);
            this.queryData = null;
            this.callbackData = null;
            this.status = this.initiationData.getStatus();
        } else if (data.containsKey("id") && data.containsKey("reference")) {
            this.initiationData = null;
            this.queryData = new QueryDataDto(data);
            this.callbackData = new CallbackDataDto(data);
            this.status = this.queryData.getStatus();
        } else {
            this.initiationData = null;
            this.queryData = null;
            this.callbackData = null;
            String statusValue = data.containsKey("status") ? (String) data.get("status") : "pending";
            this.status = PaymentStatus.fromString(statusValue);
        }
    }

    public PaymentStatus getStatus() {
        return status;
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

