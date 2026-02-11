package co.ke.tinker.config;

public class Configuration {
    private final String apiPublicKey;
    private final String apiSecretKey;
    private final String baseUrl;
    private final String authUrl;

    public Configuration(String apiPublicKey, String apiSecretKey) {
        this(apiPublicKey, apiSecretKey, null);
    }

    public Configuration(String apiPublicKey, String apiSecretKey, String baseUrl) {
        this.apiPublicKey = apiPublicKey;
        this.apiSecretKey = apiSecretKey;

        String resolvedBaseUrl = baseUrl;
        if (resolvedBaseUrl == null || resolvedBaseUrl.trim().isEmpty()) {
            resolvedBaseUrl = isSandboxCredentials()
                    ? Endpoints.SANDBOX_BASE_URL
                    : Endpoints.PRODUCTION_BASE_URL;
        }

        resolvedBaseUrl = resolvedBaseUrl.replaceAll("/$", "");
        if (!resolvedBaseUrl.endsWith(Endpoints.API_VERSION_PATH)) {
            resolvedBaseUrl += Endpoints.API_VERSION_PATH;
        }

        this.baseUrl = resolvedBaseUrl + "/";
        this.authUrl = resolvedBaseUrl + Endpoints.AUTH_TOKEN_PATH;
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

    public String getAuthUrl() {
        return authUrl;
    }

    private boolean isSandboxCredentials() {
        return apiPublicKey.startsWith("pk_test_") || apiSecretKey.startsWith("sk_test_");
    }
}
