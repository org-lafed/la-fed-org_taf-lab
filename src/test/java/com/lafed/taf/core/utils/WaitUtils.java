package com.lafed.taf.core.utils;

import java.time.Duration;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Thin wrapper around explicit Selenium waits.
 */
public final class WaitUtils {

    public WebDriverWait newWait(WebDriver driver, Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    public WebElement untilVisible(WebDriver driver, By locator, Duration timeout) {
        return newWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement untilClickable(WebDriver driver, By locator, Duration timeout) {
        return newWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void untilTitleContains(WebDriver driver, String value, Duration timeout) {
        newWait(driver, timeout).until(ExpectedConditions.titleContains(value));
    }

    public void untilUrlContains(WebDriver driver, String value, Duration timeout) {
        newWait(driver, timeout).until(ExpectedConditions.urlContains(value));
    }

    public void untilInvisible(WebDriver driver, By locator, Duration timeout) {
        newWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public <T> T until(WebDriver driver, Duration timeout, Function<WebDriver, T> condition) {
        return newWait(driver, timeout).until(condition);
    }
}
