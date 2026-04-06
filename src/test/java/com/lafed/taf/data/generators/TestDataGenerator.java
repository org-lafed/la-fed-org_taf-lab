package com.lafed.taf.data.generators;

import java.util.UUID;

/**
 * Generic data token generator with no business semantics.
 */
public final class TestDataGenerator {

    public String token(String prefix) {
        String effectivePrefix = prefix == null || prefix.isBlank() ? "value" : prefix.trim();
        return effectivePrefix + "-" + UUID.randomUUID();
    }
}
