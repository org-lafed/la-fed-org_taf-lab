package com.lafed.taf.data.generators;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FakeDataGenerator {
    private FakeDataGenerator() {
    }

    public static String uniqueEmail(String prefix) {
        return prefix + "+" + timestamp() + "@example.test";
    }

    public static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
