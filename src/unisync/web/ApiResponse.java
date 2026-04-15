package unisync.web;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ApiResponse {
    private ApiResponse() {}

    public static Map<String, Object> success() {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("status", "success");
        return res;
    }

    public static Map<String, Object> success(String key, Object value) {
        Map<String, Object> res = success();
        res.put(key, value);
        return res;
    }

    public static Map<String, Object> fail(String message) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("status", "fail");
        res.put("message", message);
        return res;
    }
}

