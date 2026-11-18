package co.ke.tinker.webhook.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerEventDataDto {
    private final List<String> transferredFrom;
    private final List<String> transferredTo;

    public CustomerEventDataDto(Map<String, Object> data) {
        this.transferredFrom = castList(data.get("transferred_from"));
        this.transferredTo = castList(data.get("transferred_to"));
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("transferred_from", transferredFrom);
        result.put("transferred_to", transferredTo);
        return result;
    }

    public List<String> getTransferredFrom() { return transferredFrom; }
    public List<String> getTransferredTo() { return transferredTo; }

    @SuppressWarnings("unchecked")
    private static List<String> castList(Object value) {
        return value instanceof List ? (List<String>) value : List.of();
    }
}
