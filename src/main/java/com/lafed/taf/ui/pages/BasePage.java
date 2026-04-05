package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.components.CookieConsentComponent;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Base class for page objects and components.
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final ExecutionConfig config;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver, ExecutionConfig config) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = new WaitUtils(driver, config);
    }

    public abstract boolean isLoaded();

    protected void openPath(String path) {
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        driver.get(config.getBaseUrl() + normalizedPath);
    }

    protected WebElement find(By locator) {
        return waitUtils.untilVisible(locator);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        waitUtils.untilClickable(locator).click();
    }

    protected void type(By locator, String value) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected boolean isDisplayed(By locator) {
        return !driver.findElements(locator).isEmpty() && driver.findElement(locator).isDisplayed();
    }

    protected String textOf(By locator) {
        return find(locator).getText().trim();
    }

    public CookieConsentComponent cookieConsent() {
        return new CookieConsentComponent(driver, config);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String title() {
        return driver.getTitle();
    }
}