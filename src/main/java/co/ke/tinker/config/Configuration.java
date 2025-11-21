package co.ke.tinker.config;

public class Configuration {
    private final String apiPublicKey;
    private final String apiSecretKey;
    private final String baseUrl;

    public Configuration(String apiPublicKey, String apiSecretKey) {
        this.apiPublicKey = apiPublicKey;
        this.apiSecretKey = apiSecretKey;
        this.baseUrl = Endpoints.API_BASE_URL + "/";
    }

    public String getApiPublicKey() {
        return apiPublicKey;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public String getApiKey() {
        return apiSecretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}

