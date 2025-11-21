package co.ke.tinker.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    void testInitialize() {
        Configuration config = new Configuration("pk_test_123", "sk_test_456");

        assertEquals("pk_test_123", config.getApiPublicKey());
        assertEquals("sk_test_456", config.getApiSecretKey());
        assertEquals(Endpoints.API_BASE_URL + "/", config.getBaseUrl());
    }

    @Test
    void testGetApiKey() {
        Configuration config = new Configuration("pk_test_123", "sk_test_456");

        assertEquals("sk_test_456", config.getApiKey());
    }
}

