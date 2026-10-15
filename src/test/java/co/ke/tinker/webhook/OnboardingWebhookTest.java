package co.ke.tinker.webhook;

import co.ke.tinker.exception.InvalidPayloadException;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class OnboardingWebhookTest {
    @Test
    void verifiesGoSetupTestWithoutCreatingPayment() throws Exception {
        String payload;
        try (var input = getClass().getResourceAsStream("/setup-webhook.json")) {
            assertNotNull(input);
            payload = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        WebhookHandler handler = new WebhookHandler();
        WebhookEvent event = handler.handle(payload);
        assertTrue(event.isOnboardingEvent());
        assertTrue(event.isSetupTest());
        assertTrue(event.getOnboardingData().getMessage().contains("webhook verification"));
        assertNull(event.getPaymentData());
        assertNull(handler.handleAsTransaction(payload));
        assertTrue(handler.verifySignature(event, "webhook-secret"));
        assertTrue(handler.verifySignature(payload, "webhook-secret"));
        assertFalse(handler.verifySignature(payload, "wrong-secret"));
        assertFalse(handler.verifySignature(payload, ""));
        assertFalse(handler.verifySignature(payload.replace("verification", "forged"), "webhook-secret"));
        assertFalse(handler.verifySignature(payload.replace("HMAC-SHA256", "none"), "webhook-secret"));
        assertFalse(handler.verifySignature(payload.replace("sha256=", "invalid="), "webhook-secret"));
        assertFalse(handler.handle(payload.replace("setup.webhook_test", "onboarding.future")).isSetupTest());
        String unknown = payload.replace("\"source\":\"onboarding\"", "\"source\":\"unknown\"");
        assertThrows(InvalidPayloadException.class, () -> handler.handle(unknown));
    }
}
