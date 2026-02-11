package co.ke.tinker.model.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSubscriptionPlanRequestDto {
    private final String name;
    private final double amount;
    private final String currency;
    private final List<String> intervals;
    private final String description;
    private final boolean isActive;

    public CreateSubscriptionPlanRequestDto(String name, double amount, String currency, List<String> intervals) {
        this(name, amount, currency, intervals, "", true);
    }

    public CreateSubscriptionPlanRequestDto(String name, double amount, String currency, List<String> intervals, String description, boolean isActive) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.intervals = intervals;
        this.description = description;
        this.isActive = isActive;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("amount", amount);
        payload.put("currency", currency);
        payload.put("intervals", intervals);
        payload.put("is_active", isActive);
        return payload;
    }
}
