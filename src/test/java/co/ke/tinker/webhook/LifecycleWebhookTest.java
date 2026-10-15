package co.ke.tinker.webhook;

import co.ke.tinker.model.lifecycle.CancellationStatus;
import co.ke.tinker.model.lifecycle.PayoutStatus;
import co.ke.tinker.model.lifecycle.RefundStatus;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class LifecycleWebhookTest {
    @Test
    void verifiesLifecycleEvidenceWithoutCreatingPaymentTransactions() throws Exception {
        WebhookHandler handler = new WebhookHandler();
        for (String fixture : new String[] {"checkout-cancellation-webhook.json", "refund-webhook.json", "withdrawal-webhook.json"}) {
            String payload;
            try (var input = getClass().getResourceAsStream("/" + fixture)) {
                assertNotNull(input);
                payload = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            }
            WebhookEvent event = handler.handle(payload);
            assertTrue(handler.verifySignature(event, "webhook-secret"), fixture);
            switch (fixture) {
                case "checkout-cancellation-webhook.json":
                    assertTrue(event.isCheckoutCancellationEvent());
                    assertEquals(CancellationStatus.OUTCOME_UNKNOWN, event.getCancellationData().getStatus());
                    assertNotNull(event.getCancellationData().getPaymentId());
                    break;
                case "refund-webhook.json":
                    assertTrue(event.isRefundEvent());
                    assertEquals(RefundStatus.PENDING_REVIEW, event.getRefundData().getStatus());
                    assertEquals("app-1", event.getRefundData().getRequestedByAppId());
                    break;
                case "withdrawal-webhook.json":
                    assertTrue(event.isWithdrawalEvent());
                    assertEquals(PayoutStatus.PENDING_APPROVAL, event.getWithdrawalData().getStatus());
                    assertEquals(98.0, event.getWithdrawalData().getNetAmount());
                    break;
                default:
                    fail("Unhandled fixture " + fixture);
            }
            assertNull(event.getPaymentData());
            assertNull(event.toTransaction());
            assertNull(handler.handleAsTransaction(payload));
            assertFalse(handler.verifySignature(event, "wrong-secret"));
            assertFalse(handler.verifySignature(payload.replace("app-1", "forged"), "webhook-secret"));
            WebhookEvent future = handler.handle(payload.replace(".updated.v1", ".updated.v2"));
            assertFalse(future.isCheckoutCancellationEvent());
            assertFalse(future.isRefundEvent());
            assertFalse(future.isWithdrawalEvent());
            assertNull(future.toTransaction());
        }
    }
}
