package co.ke.tinker.enums;

public enum Gateway {
    MPESA("mpesa"),
    PAYSTACK("paystack"),
    STRIPE("stripe"),
    PESAPAL("pesapal");

    private final String value;

    Gateway(String value) {
        this.value = value;
    }

    public static Gateway fromValue(String value) {
        for (Gateway gateway : values()) {
            if (gateway.value.equals(value)) return gateway;
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

