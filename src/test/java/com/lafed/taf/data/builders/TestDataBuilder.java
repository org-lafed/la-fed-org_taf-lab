package com.lafed.taf.data.builders;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Small generic builder for future test payload assembly.
 */
public final class TestDataBuilder {

    private final Map<String, Object> values = new LinkedHashMap<>();

    public TestDataBuilder with(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return Map.copyOf(values);
    }
}
