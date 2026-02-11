package co.ke.tinker.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationTest {
    @Test
    void testInitialize() {
        Configuration config = new Configuration("pk_test_123", "sk_test_456");

        assertEquals("pk_test_123", config.getApiPublicKey());
        assertEquals("sk_test_456", config.getApiSecretKey());
        assertEquals("https://sandbox-api.tinkerpayments.com/v1/", config.getBaseUrl());
        assertEquals("https://sandbox-api.tinkerpayments.com/v1/auth/token", config.getAuthUrl());
    }

    @Test
    void testLiveDefaultsToProduction() {
        Configuration config = new Configuration("pk_live_123", "sk_live_456");

        assertEquals("https://api.tinkerpayments.com/v1/", config.getBaseUrl());
        assertEquals("https://api.tinkerpayments.com/v1/auth/token", config.getAuthUrl());
    }

    @Test
    void testOverrideBaseUrl() {
        Configuration config = new Configuration("pk_test_123", "sk_test_456", "http://localhost:8080");

        assertEquals("http://localhost:8080/v1/", config.getBaseUrl());
        assertEquals("http://localhost:8080/v1/auth/token", config.getAuthUrl());
    }

    @Test
    void testGetApiKey() {
        Configuration config = new Configuration("pk_test_123", "sk_test_456");

        assertEquals("sk_test_456", config.getApiKey());
    }
}
