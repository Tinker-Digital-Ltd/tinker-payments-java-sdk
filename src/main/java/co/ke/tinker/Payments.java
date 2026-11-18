package co.ke.tinker;

import co.ke.tinker.api.CustomerManager;
import co.ke.tinker.api.EntitlementManager;
import co.ke.tinker.api.ProductManager;
import co.ke.tinker.api.OfferingManager;
import co.ke.tinker.api.MetricsManager;
import co.ke.tinker.api.ExperimentManager;
import co.ke.tinker.api.IntegrationManager;
import co.ke.tinker.api.PayoutManager;
import co.ke.tinker.api.SubscriptionManager;
import co.ke.tinker.api.TransactionManager;
import co.ke.tinker.auth.AuthenticationManager;
import co.ke.tinker.config.Configuration;
import co.ke.tinker.http.HttpClient;
import co.ke.tinker.model.ApiMeta;
import co.ke.tinker.webhook.WebhookHandler;

public class Payments {
    private final Configuration config;
    private final HttpClient httpClient;
    private final AuthenticationManager authManager;
    private PayoutManager payoutManager;
    private TransactionManager transactionsManager;
    private SubscriptionManager subscriptionManager;
    private CustomerManager customerManager;
    private EntitlementManager entitlementManager;
    private ProductManager productManager;
    private OfferingManager offeringManager;
    private MetricsManager metricsManager;
    private ExperimentManager experimentManager;
    private IntegrationManager integrationManager;
    private WebhookHandler webhookHandler;

    public Payments(String apiPublicKey, String apiSecretKey) {
        this(apiPublicKey, apiSecretKey, null, null);
    }

    public Payments(String apiPublicKey, String apiSecretKey, HttpClient httpClient) {
        this(apiPublicKey, apiSecretKey, httpClient, null);
    }

    public Payments(String apiPublicKey, String apiSecretKey, HttpClient httpClient, String baseUrl) {
        this.config = new Configuration(apiPublicKey, apiSecretKey, baseUrl);
        this.httpClient = httpClient != null ? httpClient : new HttpClient();
        this.authManager = new AuthenticationManager(this.config, this.httpClient);
    }

    public TransactionManager transactions() {
        if (transactionsManager == null) {
            transactionsManager = new TransactionManager(config, httpClient, authManager);
        }
        return transactionsManager;
    }

    public SubscriptionManager subscriptions() {
        if (subscriptionManager == null) {
            subscriptionManager = new SubscriptionManager(config, httpClient, authManager);
        }
        return subscriptionManager;
    }

    public PayoutManager payouts() {
        if (payoutManager == null) payoutManager = new PayoutManager(config, httpClient, authManager);
        return payoutManager;
    }

    public CustomerManager customers() {
        if (customerManager == null) customerManager = new CustomerManager(config, httpClient, authManager);
        return customerManager;
    }

    public EntitlementManager entitlements() {
        if (entitlementManager == null) entitlementManager = new EntitlementManager(config, httpClient, authManager);
        return entitlementManager;
    }

    public ProductManager products() {
        if (productManager == null) productManager = new ProductManager(config, httpClient, authManager);
        return productManager;
    }

    public OfferingManager offerings() {
        if (offeringManager == null) offeringManager = new OfferingManager(config, httpClient, authManager);
        return offeringManager;
    }

    public MetricsManager metrics() {
        if (metricsManager == null) metricsManager = new MetricsManager(config, httpClient, authManager);
        return metricsManager;
    }

    public ExperimentManager experiments() {
        if (experimentManager == null) experimentManager = new ExperimentManager(config, httpClient, authManager);
        return experimentManager;
    }

    public IntegrationManager integrations() {
        if (integrationManager == null) integrationManager = new IntegrationManager(config, httpClient, authManager);
        return integrationManager;
    }

    public WebhookHandler webhooks() {
        if (webhookHandler == null) {
            webhookHandler = new WebhookHandler();
        }
        return webhookHandler;
    }

    public ApiMeta getLastAuthMeta() {
        return authManager.getLastMeta();
    }
}
