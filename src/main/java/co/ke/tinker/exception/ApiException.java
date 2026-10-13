package co.ke.tinker.exception;

import java.util.List;
import java.util.Map;

public class ApiException extends RuntimeException {
    private final int code;
    private final Integer httpStatus;
    private final String errorCode;
    private final String providerCode;
    private final String requestId;
    private final String retryAfter;
    private final String outcome;

    public ApiException(String message) {
        this(message, ExceptionCode.API_ERROR);
    }

    public ApiException(String message, int code) {
        this(message, code, null, null, null);
    }

    public ApiException(String message, int code, Integer httpStatus, Map<String, Object> details,
                        Map<String, List<String>> headers) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        Object error = details == null ? null : details.get("error");
        Map<?, ?> fields = error instanceof Map ? (Map<?, ?>) error : Map.of();
        this.errorCode = stringField(fields, "code");
        this.providerCode = stringField(fields, "provider_code");
        String bodyRequestId = stringField(fields, "request_id");
        this.requestId = bodyRequestId == null || bodyRequestId.isEmpty() ? header(headers, "X-Request-ID") : bodyRequestId;
        this.retryAfter = header(headers, "Retry-After");
        this.outcome = stringField(fields, "outcome");
    }

    private static String stringField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        return value instanceof String ? (String) value : null;
    }

    private static String header(Map<String, List<String>> headers, String name) {
        if (headers == null) return null;
        return headers.entrySet().stream()
                .filter(entry -> name.equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(values -> values != null && !values.isEmpty())
                .map(values -> values.get(0))
                .findFirst().orElse(null);
    }

    public int getCode() { return code; }
    public Integer getHttpStatus() { return httpStatus; }
    public String getErrorCode() { return errorCode; }
    public String getProviderCode() { return providerCode; }
    public String getRequestId() { return requestId; }
    public String getRetryAfter() { return retryAfter; }
    /** Only an explicit not_applied confirms no mutation. */
    public String getOutcome() { return outcome; }
}
