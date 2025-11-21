package co.ke.tinker.exception;

public class WebhookException extends RuntimeException {
    private final int code;

    public WebhookException(String message) {
        this(message, ExceptionCode.WEBHOOK_ERROR);
    }

    public WebhookException(String message, int code) {
        super(message);
        this.code = code;
    }

    public WebhookException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

