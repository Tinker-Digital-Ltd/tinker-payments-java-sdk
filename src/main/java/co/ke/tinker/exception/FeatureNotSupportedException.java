package co.ke.tinker.exception;

import java.util.List;
import java.util.Map;

public class FeatureNotSupportedException extends ApiException {
    public static final String CODE = "FEATURE_NOT_SUPPORTED";

    private final String gateway;
    private final String feature;
    private final List<String> alternatives;

    public FeatureNotSupportedException(String message, int code, Integer httpStatus, Map<String, Object> details,
                                        Map<String, List<String>> headers) {
        super(message, code, httpStatus, details, headers);
        Object error = details == null ? null : details.get("error");
        Object info = error instanceof Map ? ((Map<?, ?>) error).get("details") : null;
        Map<?, ?> fields = info instanceof Map ? (Map<?, ?>) info : Map.of();
        this.gateway = fields.get("gateway") instanceof String ? (String) fields.get("gateway") : null;
        this.feature = fields.get("feature") instanceof String ? (String) fields.get("feature") : null;
        Object alts = fields.get("alternatives");
        this.alternatives = alts instanceof List
                ? ((List<?>) alts).stream().map(String::valueOf).collect(java.util.stream.Collectors.toUnmodifiableList())
                : List.of();
    }

    public String getGateway() { return gateway; }
    public String getFeature() { return feature; }
    public List<String> getAlternatives() { return alternatives; }
}
