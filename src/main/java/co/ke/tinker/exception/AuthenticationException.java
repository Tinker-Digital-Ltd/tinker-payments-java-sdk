package co.ke.tinker.exception;

public class AuthenticationException extends RuntimeException {
    private final int code;

    public AuthenticationException(String message) {
        this(message, ExceptionCode.AUTHENTICATION_ERROR);
    }

    public AuthenticationException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AuthenticationException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

