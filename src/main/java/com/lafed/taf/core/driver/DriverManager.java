package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.WebDriver;

/**
 * Thread-local WebDriver lifecycle manager.
 */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver createDriver(ExecutionConfig config) {
        WebDriver driver = DriverFactory.createDriver(config);
        DRIVER_HOLDER.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_HOLDER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized for the current thread.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_HOLDER.get();
        if (driver != null) {
            driver.quit();
            DRIVER_HOLDER.remove();
        }
    }
}
