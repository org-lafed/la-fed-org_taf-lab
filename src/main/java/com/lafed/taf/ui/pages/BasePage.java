package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.UiInterferenceGuard;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.components.CookieConsentComponent;
import com.lafed.taf.ui.components.SubscriptionComponent;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base class for page objects and components.
 */
public abstract class BasePage {
    protected final WebDriver driver;
    protected final ExecutionConfig config;
    protected final WaitUtils waitUtils;
    protected final UiInterferenceGuard uiInterferenceGuard;

    protected BasePage(WebDriver driver, ExecutionConfig config) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = new WaitUtils(driver, config);
        this.uiInterferenceGuard = new UiInterferenceGuard(driver, config);
    }

    public abstract boolean isLoaded();

    protected void openPath(String path) {
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        driver.get(config.getBaseUrl() + normalizedPath);
        uiInterferenceGuard.afterNavigation();
    }

    protected WebElement find(By locator) {
        return waitUtils.untilVisible(locator);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        uiInterferenceGuard.beforeInteraction();
        scrollIntoView(locator);
        try {
            waitUtils.untilClickable(locator).click();
        } catch (ElementClickInterceptedException exception) {
            uiInterferenceGuard.recoverFromInterference();
            scrollIntoView(locator);
            waitUtils.untilClickable(locator).click();
        }
    }

    protected void type(By locator, String value) {
        uiInterferenceGuard.beforeInteraction();
        scrollIntoView(locator);
        try {
            WebElement element = find(locator);
            element.clear();
            element.sendKeys(value);
        } catch (ElementClickInterceptedException exception) {
            uiInterferenceGuard.recoverFromInterference();
            scrollIntoView(locator);
            WebElement element = find(locator);
            element.clear();
            element.sendKeys(value);
        }
    }

    protected boolean isDisplayed(By locator) {
        return isDisplayedSafely(locator);
    }

    protected boolean isDisplayedSafely(By locator) {
        try {
            return driver.findElements(locator).stream().anyMatch(this::isElementDisplayedSafely);
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    protected boolean hasAnyVisible(By locator) {
        return findAll(locator).stream().anyMatch(this::isElementDisplayedSafely);
    }

    protected String textOf(By locator) {
        return find(locator).getText().trim();
    }

    public CookieConsentComponent cookieConsent() {
        return new CookieConsentComponent(driver, config);
    }

    public SubscriptionComponent subscription() {
        return new SubscriptionComponent(driver, config);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String title() {
        return driver.getTitle();
    }

    protected void waitForDocumentReady() {
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until((ExpectedCondition<Boolean>) ignored -> {
                    try {
                        Object readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState");
                        return "complete".equals(String.valueOf(readyState));
                    } catch (RuntimeException exception) {
                        return false;
                    }
                });
    }

    protected void scrollIntoView(By locator) {
        WebElement element = find(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                element);
    }

    protected void selectByVisibleText(By locator, String text) {
        uiInterferenceGuard.beforeInteraction();
        scrollIntoView(locator);
        new Select(find(locator)).selectByVisibleText(text);
    }

    protected void selectByVisibleTextRobustly(By locator, String text) {
        uiInterferenceGuard.beforeInteraction();
        WebElement selectElement = find(locator);
        scrollIntoView(locator);

        try {
            new Select(selectElement).selectByVisibleText(text);
        } catch (ElementClickInterceptedException exception) {
            uiInterferenceGuard.recoverFromInterference();
            selectElement = find(locator);
            scrollIntoView(locator);
            selectByVisibleTextWithJs(selectElement, text);
        }
    }

    private void selectByVisibleTextWithJs(WebElement selectElement, String text) {
        String value = new Select(selectElement).getOptions().stream()
                .filter(option -> option.getText().trim().equals(text))
                .map(option -> option.getAttribute("value"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option not found for text: " + text));

        ((JavascriptExecutor) driver).executeScript(
                "const select = arguments[0];"
                        + "const value = arguments[1];"
                        + "select.value = value;"
                        + "select.dispatchEvent(new Event('change', { bubbles: true }));",
                selectElement,
                value);
    }

    private boolean isElementDisplayedSafely(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (RuntimeException ignored) {
            return false;
        }
    }
}
