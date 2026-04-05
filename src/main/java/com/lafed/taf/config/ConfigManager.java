package com.lafed.taf.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Loads execution settings from environment-specific properties and environment variables.
 */
public final class ConfigManager {
    private static volatile ExecutionConfig config;

    private ConfigManager() {
    }

    public static ExecutionConfig getConfig() {
        if (config == null) {
            synchronized (ConfigManager.class) {
                if (config == null) {
                    config = load();
                }
            }
        }
        return config;
    }

    public static synchronized ExecutionConfig load() {
        String env = firstNonBlank(
                System.getProperty("env"),
                System.getenv("TAF_ENV"),
                "local");

        Properties properties = new Properties();
        String resourcePath = "config/application-" + env + ".properties";
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing configuration resource: " + resourcePath);
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration: " + resourcePath, exception);
        }

        Map<String, String> envVars = System.getenv();
        config = new ExecutionConfig(
                env,
                resolve(properties, envVars, "base.url", "TAF_BASE_URL"),
                resolve(properties, envVars, "api.base.url", "TAF_API_BASE_URL"),
                resolve(properties, envVars, "browser", "TAF_BROWSER"),
                Boolean.parseBoolean(resolve(properties, envVars, "headless", "TAF_HEADLESS")),
                Integer.parseInt(resolve(properties, envVars, "window.width", "TAF_WINDOW_WIDTH")),
                Integer.parseInt(resolve(properties, envVars, "window.height", "TAF_WINDOW_HEIGHT")),
                Integer.parseInt(resolve(properties, envVars, "explicit.timeout.seconds", "TAF_EXPLICIT_TIMEOUT_SECONDS")),
                Integer.parseInt(resolve(properties, envVars, "page.load.timeout.seconds", "TAF_PAGE_LOAD_TIMEOUT_SECONDS")),
                Boolean.parseBoolean(resolve(properties, envVars, "screenshot.on.failure", "TAF_SCREENSHOT_ON_FAILURE"))
        );
        return config;
    }

    public static synchronized void reset() {
        config = null;
    }

    private static String resolve(Properties properties, Map<String, String> envVars, String propertyKey, String envKey) {
        return firstNonBlank(
                System.getProperty(propertyKey),
                envVars.get(envKey),
                properties.getProperty(propertyKey));
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return Objects.requireNonNull(values[values.length - 1], "No fallback value available");
    }
}
