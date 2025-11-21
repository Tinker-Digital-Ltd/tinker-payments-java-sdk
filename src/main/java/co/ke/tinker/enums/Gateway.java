package co.ke.tinker.enums;

public enum Gateway {
    MPESA("mpesa"),
    PAYSTACK("paystack"),
    STRIPE("stripe");

    private final String value;

    Gateway(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

