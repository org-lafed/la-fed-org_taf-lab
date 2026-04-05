package com.lafed.taf.config;

/**
 * Immutable execution configuration resolved from properties and environment variables.
 */
public final class ExecutionConfig {
    private final String env;
    private final String baseUrl;
    private final String apiBaseUrl;
    private final String browser;
    private final boolean headless;
    private final int windowWidth;
    private final int windowHeight;
    private final int explicitTimeoutSeconds;
    private final int pageLoadTimeoutSeconds;
    private final boolean screenshotOnFailure;

    public ExecutionConfig(
            String env,
            String baseUrl,
            String apiBaseUrl,
            String browser,
            boolean headless,
            int windowWidth,
            int windowHeight,
            int explicitTimeoutSeconds,
            int pageLoadTimeoutSeconds,
            boolean screenshotOnFailure) {
        this.env = env;
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.apiBaseUrl = trimTrailingSlash(apiBaseUrl);
        this.browser = browser;
        this.headless = headless;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.explicitTimeoutSeconds = explicitTimeoutSeconds;
        this.pageLoadTimeoutSeconds = pageLoadTimeoutSeconds;
        this.screenshotOnFailure = screenshotOnFailure;
    }

    public String getEnv() {
        return env;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean isHeadless() {
        return headless;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getExplicitTimeoutSeconds() {
        return explicitTimeoutSeconds;
    }

    public int getPageLoadTimeoutSeconds() {
        return pageLoadTimeoutSeconds;
    }

    public boolean isScreenshotOnFailure() {
        return screenshotOnFailure;
    }

    private static String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
