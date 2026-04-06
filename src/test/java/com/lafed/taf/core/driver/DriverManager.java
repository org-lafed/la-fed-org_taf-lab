package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.WebDriver;

/**
 * Owns the thread-bound WebDriver lifecycle for future UI execution.
 */
public final class DriverManager {

    private final DriverFactory driverFactory;
    private final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    public DriverManager(DriverFactory driverFactory) {
        this.driverFactory = driverFactory;
    }

    public void start(ExecutionConfig config) {
        stop();
        drivers.set(driverFactory.createDriver(config));
    }

    public WebDriver getDriver() {
        return drivers.get();
    }

    public void stop() {
        WebDriver driver = drivers.get();
        if (driver != null) {
            driver.quit();
            drivers.remove();
        }
    }
}
