package com.lafed.taf.core.utils;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Convenience wrapper for explicit waits.
 */
public class WaitUtils {
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver, ExecutionConfig config) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()));
    }

    public WebElement untilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement untilClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean untilUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public boolean untilTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    public boolean untilInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
