package co.ke.tinker.api;

import co.ke.tinker.model.Transaction;
import co.ke.tinker.model.dto.InitiatePaymentRequestDto;
import co.ke.tinker.model.dto.QueryPaymentRequestDto;
import co.ke.tinker.config.Endpoints;

import java.util.Map;
import co.ke.tinker.model.lifecycle.*;

public class TransactionManager extends BaseManager {
    public TransactionManager(co.ke.tinker.config.Configuration config, co.ke.tinker.http.HttpClient httpClient, co.ke.tinker.auth.AuthenticationManager authManager) {
        super(config, httpClient, authManager);
    }

    public Transaction initiate(InitiatePaymentRequestDto request) {
        Map<String, Object> payload = request.toMap();
        Map<String, Object> response = request("POST", Endpoints.PAYMENT_INITIATE_PATH, payload);
        return new Transaction(response);
    }

    public Transaction query(QueryPaymentRequestDto request) {
        Map<String, Object> payload = request.toMap();
        Map<String, Object> response = request("POST", Endpoints.PAYMENT_QUERY_PATH, payload);
        return new Transaction(response);
    }

    public RefundResult refund(String paymentId, RefundRequest payload, String key) {
        if (payload.amount != null) LifecycleCodec.positiveAmount(payload.amount);
        return LifecycleCodec.decode(request("POST", LifecycleCodec.payment(paymentId) + "/refund", LifecycleCodec.payload(payload), LifecycleCodec.headers(key)), RefundResult.class);
    }
    public RefundHistory refunds(String paymentId) {
        return LifecycleCodec.decode(request("GET", LifecycleCodec.payment(paymentId) + "/refunds", null), RefundHistory.class);
    }
    public Refund getRefund(String paymentId, String refundId) {
        return LifecycleCodec.decode(request("GET", LifecycleCodec.payment(paymentId) + "/refunds/" + LifecycleCodec.encode(refundId), null), Refund.class);
    }
    public CancellationResult cancelCheckout(String paymentId, CancellationRequest payload, String key) {
        return LifecycleCodec.decode(request("POST", LifecycleCodec.payment(paymentId) + "/cancellations", LifecycleCodec.payload(payload), LifecycleCodec.headers(key)), CancellationResult.class);
    }
    public CancellationHistory cancellations(String paymentId) {
        return LifecycleCodec.decode(request("GET", LifecycleCodec.payment(paymentId) + "/cancellations", null), CancellationHistory.class);
    }
    public Cancellation getCancellation(String paymentId, String requestId) {
        return LifecycleCodec.decode(request("GET", LifecycleCodec.payment(paymentId) + "/cancellations/" + LifecycleCodec.encode(requestId), null), Cancellation.class);
    }
}

