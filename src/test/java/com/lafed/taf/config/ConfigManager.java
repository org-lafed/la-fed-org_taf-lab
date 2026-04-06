package com.lafed.taf.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Resolves the active environment and loads the matching properties resource.
 */
public final class ConfigManager {

    private static final String DEFAULT_ENV = "local";

    private ConfigManager() {
    }

    public static ExecutionConfig load() {
        return ExecutionConfig.from(loadProperties(resolveEnv()));
    }

    public static String resolveEnv() {
        String envFromProperty = System.getProperty("env");
        if (envFromProperty != null && !envFromProperty.isBlank()) {
            return envFromProperty.trim();
        }

        String envFromVariable = System.getenv("TAF_ENV");
        if (envFromVariable != null && !envFromVariable.isBlank()) {
            return envFromVariable.trim();
        }

        return DEFAULT_ENV;
    }

    public static Properties loadProperties(String env) {
        String resourcePath = "config/application-" + env + ".properties";
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing configuration resource: " + resourcePath);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read configuration resource: " + resourcePath, exception);
        }
    }
}
