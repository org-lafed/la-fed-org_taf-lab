package com.lafed.taf.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Resource loading helpers for test data and schemas.
 */
public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static InputStream getResourceAsStream(String path) {
        InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return inputStream;
    }

    public static String readResourceAsString(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read resource: " + path, exception);
        }
    }
}
