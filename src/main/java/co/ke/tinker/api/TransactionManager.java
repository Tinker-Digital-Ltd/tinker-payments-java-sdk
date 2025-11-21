package co.ke.tinker.api;

import co.ke.tinker.model.Transaction;
import co.ke.tinker.model.dto.InitiatePaymentRequestDto;
import co.ke.tinker.model.dto.QueryPaymentRequestDto;
import co.ke.tinker.config.Endpoints;

import java.util.Map;

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
}

