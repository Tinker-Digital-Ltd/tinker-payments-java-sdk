package co.ke.tinker.exception;

public class InvalidPayloadException extends RuntimeException {
    private final int code;

    public InvalidPayloadException(String message) {
        this(message, ExceptionCode.INVALID_PAYLOAD);
    }

    public InvalidPayloadException(String message, int code) {
        super(message);
        this.code = code;
    }

    public InvalidPayloadException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

