package com.lafed.taf.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Resolves the active environment, loads the matching properties resource, and applies overrides.
 */
public final class ConfigManager {

    private static final String DEFAULT_ENV = "local";

    private ConfigManager() {
    }

    public static ExecutionConfig load() {
        Properties properties = loadProperties(resolveEnv());
        applyOverride(properties, "env.name", "env", "TAF_ENV");
        applyOverride(properties, "ui.base.url", "base.url", "TAF_BASE_URL");
        applyOverride(properties, "api.base.url", "api.base.url", "TAF_API_BASE_URL");
        applyOverride(properties, "browser.name", "browser", "TAF_BROWSER");
        applyOverride(properties, "browser.binary.path", "browser.binary.path", "TAF_BROWSER_BINARY");
        applyOverride(properties, "browser.headless", "headless", "TAF_HEADLESS");
        applyOverride(properties, "timeout.explicit.seconds", "explicit.timeout.seconds", "TAF_EXPLICIT_TIMEOUT_SECONDS");
        applyOverride(properties, "timeout.page.load.seconds", "page.load.timeout.seconds", "TAF_PAGE_LOAD_TIMEOUT_SECONDS");
        applyOverride(properties, "screenshot.on.failure", "screenshot.on.failure", "TAF_SCREENSHOT_ON_FAILURE");
        applyOverride(properties, "smoke.login.email", "smoke.login.email", "TAF_SMOKE_LOGIN_EMAIL");
        applyOverride(properties, "smoke.login.password", "smoke.login.password", "TAF_SMOKE_LOGIN_PASSWORD");
        applyOverride(properties, "smoke.login.display.name", "smoke.login.display.name", "TAF_SMOKE_LOGIN_DISPLAY_NAME");
        return ExecutionConfig.from(properties);
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

    private static void applyOverride(Properties properties, String targetKey, String systemPropertyKey, String envVariableKey) {
        String systemValue = System.getProperty(systemPropertyKey);
        if (systemValue != null && !systemValue.isBlank()) {
            properties.setProperty(targetKey, systemValue.trim());
            return;
        }

        String envValue = System.getenv(envVariableKey);
        if (envValue != null && !envValue.isBlank()) {
            properties.setProperty(targetKey, envValue.trim());
        }
    }
}
