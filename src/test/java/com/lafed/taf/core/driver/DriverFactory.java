package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.WebDriver;

/**
 * Placeholder browser factory. Concrete browser bootstrap is intentionally absent.
 */
public final class DriverFactory {

    public WebDriver createDriver(ExecutionConfig config) {
        throw new UnsupportedOperationException(
                "Browser bootstrap is not implemented in this scaffold for env: " + config.envName());
    }
}
