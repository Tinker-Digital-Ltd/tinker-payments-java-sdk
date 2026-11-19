package co.ke.tinker.model.dto;

import java.util.HashMap;
import java.util.Map;

/** Promotional entitlement grant: a duration, an RFC3339 end time, or neither for access until revoked. */
public final class PromotionalGrant {
    private final String duration;
    private final String endTime;

    private PromotionalGrant(String duration, String endTime) {
        this.duration = duration;
        this.endTime = endTime;
    }

    public static PromotionalGrant untilRevoked() {
        return new PromotionalGrant(null, null);
    }

    public static PromotionalGrant ofDuration(String duration) {
        return new PromotionalGrant(duration, null);
    }

    public static PromotionalGrant endingAt(String endTime) {
        return new PromotionalGrant(null, endTime);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> body = new HashMap<>();
        if (duration != null) body.put("duration", duration);
        if (endTime != null) body.put("end_time", endTime);
        return body;
    }
}
