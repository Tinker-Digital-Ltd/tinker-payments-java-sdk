package co.ke.tinker.exception;

public class ClientException extends RuntimeException {
    private final int code;

    public ClientException(String message) {
        this(message, ExceptionCode.CLIENT_ERROR);
    }

    public ClientException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ClientException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

