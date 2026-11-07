package co.ke.tinker.enums;

public enum PaymentStatus {
    PENDING("pending"),
    AUTHORISED("authorised"),
    SUCCESS("success"),
    CANCELLED("cancelled"),
    FAILED("failed"),
    BLOCKED("blocked"),
    REFUNDED("refunded"),
    PARTIALLY_REFUNDED("partially_refunded"),
    EXPIRED("expired"),
    REVERSED("reversed"),
    UNKNOWN("unknown");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "pending";
        }
        return "completed".equalsIgnoreCase(value) ? "success" : value;
    }

    public static PaymentStatus fromString(String value) {
        try {
            return PaymentStatus.valueOf(normalize(value).toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

