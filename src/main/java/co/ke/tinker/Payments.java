package co.ke.tinker;

import co.ke.tinker.api.TransactionManager;
import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.webhook.WebhookHandler;

public class Payments {
    private final Configuration config;
    private final HttpClient httpClient;
    private final AuthenticationManager authManager;
    private TransactionManager transactionsManager;
    private WebhookHandler webhookHandler;

    public Payments(String apiPublicKey, String apiSecretKey) {
        this(apiPublicKey, apiSecretKey, null);
    }

    public Payments(String apiPublicKey, String apiSecretKey, HttpClient httpClient) {
        this.config = new Configuration(apiPublicKey, apiSecretKey);
        this.httpClient = httpClient != null ? httpClient : new HttpClient();
        this.authManager = new AuthenticationManager(this.config, this.httpClient);
    }

    public TransactionManager transactions() {
        if (transactionsManager == null) {
            transactionsManager = new TransactionManager(config, httpClient, authManager);
        }
        return transactionsManager;
    }

    public WebhookHandler webhooks() {
        if (webhookHandler == null) {
            webhookHandler = new WebhookHandler();
        }
        return webhookHandler;
    }
}

