package co.ke.tinker.enums;

public enum PaymentStatus {
    PENDING("pending"),
    SUCCESS("success"),
    CANCELLED("cancelled"),
    FAILED("failed");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentStatus fromString(String value) {
        if (value == null) {
            return PENDING;
        }
        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

