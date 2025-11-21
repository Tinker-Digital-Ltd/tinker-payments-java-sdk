package co.ke.tinker.exception;

public class ApiException extends RuntimeException {
    private final int code;

    public ApiException(String message) {
        this(message, ExceptionCode.API_ERROR);
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

