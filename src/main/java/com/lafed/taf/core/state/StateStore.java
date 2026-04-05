package com.lafed.taf.core.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-local scenario state shared across steps and flows.
 */
public final class StateStore {
    private static final ThreadLocal<Map<String, Object>> STATE = ThreadLocal.withInitial(HashMap::new);

    private StateStore() {
    }

    public static void put(String key, Object value) {
        STATE.get().put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {
        Object value = STATE.get().get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }

    public static <T> T getRequired(String key, Class<T> type) {
        T value = get(key, type);
        if (value == null) {
            throw new IllegalStateException("Missing state value for key: " + key);
        }
        return value;
    }

    public static void remove(String key) {
        STATE.get().remove(key);
    }

    public static void clear() {
        STATE.remove();
    }
}
