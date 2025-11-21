package co.ke.tinker.exception;

public class NetworkException extends RuntimeException {
    private final int code;

    public NetworkException(String message) {
        this(message, ExceptionCode.NETWORK_ERROR);
    }

    public NetworkException(String message, int code) {
        super(message);
        this.code = code;
    }

    public NetworkException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

