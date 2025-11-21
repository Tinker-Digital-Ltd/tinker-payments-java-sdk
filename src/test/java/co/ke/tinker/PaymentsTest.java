package co.ke.tinker;

import co.ke.tinker.api.TransactionManager;
import co.ke.tinker.webhook.WebhookHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsTest {
    @Test
    void testLazyInitialization() {
        Payments payments = new Payments("pk_test_123", "sk_test_456");

        TransactionManager transactions1 = payments.transactions();
        TransactionManager transactions2 = payments.transactions();

        assertSame(transactions1, transactions2);

        WebhookHandler webhooks1 = payments.webhooks();
        WebhookHandler webhooks2 = payments.webhooks();

        assertSame(webhooks1, webhooks2);
    }
}

