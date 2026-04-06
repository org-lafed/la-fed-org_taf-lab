package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import java.util.Objects;
import org.openqa.selenium.WebDriver;

/**
 * Base abstraction for future page objects.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final ExecutionConfig config;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this.driver = Objects.requireNonNull(driver, "driver");
        this.config = Objects.requireNonNull(config, "config");
        this.waitUtils = Objects.requireNonNull(waitUtils, "waitUtils");
    }

    protected void open(String relativePath) {
        driver.get(config.uiBaseUrl() + normalize(relativePath));
    }

    protected String currentTitle() {
        return driver.getTitle();
    }

    private String normalize(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return "";
        }
        return relativePath.startsWith("/") ? relativePath : "/" + relativePath;
    }
}
