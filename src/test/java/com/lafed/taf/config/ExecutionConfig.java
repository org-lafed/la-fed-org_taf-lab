package com.lafed.taf.config;

import java.time.Duration;
import java.util.Properties;

/**
 * Immutable execution settings loaded from the active environment file.
 */
public record ExecutionConfig(
        String envName,
        String uiBaseUrl,
        String apiBaseUrl,
        String browserName,
        boolean headless,
        Duration explicitTimeout,
        Duration pageLoadTimeout,
        boolean screenshotOnFailure) {

    public static ExecutionConfig from(Properties properties) {
        return new ExecutionConfig(
                properties.getProperty("env.name", "local"),
                properties.getProperty("ui.base.url", "https://example.invalid"),
                properties.getProperty("api.base.url", "https://example.invalid"),
                properties.getProperty("browser.name", "chrome"),
                Boolean.parseBoolean(properties.getProperty("browser.headless", "true")),
                Duration.ofSeconds(Long.parseLong(properties.getProperty("timeout.explicit.seconds", "5"))),
                Duration.ofSeconds(Long.parseLong(properties.getProperty("timeout.page.load.seconds", "15"))),
                Boolean.parseBoolean(properties.getProperty("screenshot.on.failure", "true")));
    }
}
