package co.ke.tinker.webhook.dto;

import java.util.Map;

public class OnboardingEventDataDto {
    private final String message;

    public OnboardingEventDataDto(Map<String, Object> data) {
        this.message = (String) data.getOrDefault("message", "");
    }

    public String getMessage() { return message; }

    public Map<String, Object> toMap() { return Map.of("message", message); }
}
