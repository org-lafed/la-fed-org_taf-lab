package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Base abstraction for page objects used by the smoke flow.
 */
public abstract class BasePage {

    private static final By CONSENT_ALLOW_BUTTON = By.xpath(
            "//button[normalize-space()='Autoriser' or normalize-space()='Accept' or normalize-space()='Accept All']");
    private static final By GENERIC_CLOSE_TEXT = By.xpath("//*[normalize-space()='Close']");
    private static final By GENERIC_CLOSE_BUTTON = By.cssSelector("button[aria-label='Close'], button[title='Close'], .modal .close");

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

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }

    protected WebElement visible(By locator) {
        return waitUtils.untilVisible(driver, locator, config.explicitTimeout());
    }

    protected WebElement clickable(By locator) {
        return waitUtils.untilClickable(driver, locator, config.explicitTimeout());
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected void type(By locator, String value) {
        WebElement element = prepareForInteraction(locator);
        element.clear();
        element.sendKeys(value);
        if (value.equals(element.getAttribute("value"))) {
            return;
        }

        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        element.sendKeys(value);
        if (value.equals(element.getAttribute("value"))) {
            return;
        }

        executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                        + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
                element,
                value);
        waitUtils.until(driver, config.explicitTimeout(),
                currentDriver -> value.equals(currentDriver.findElement(locator).getAttribute("value")));
    }

    protected void click(By locator) {
        try {
            WebElement element = prepareForInteraction(locator);
            if (!isElementUnobstructed(element)) {
                dismissBlockingInterferenceIfPresent();
                element = prepareForInteraction(locator);
            }
            element.click();
        } catch (ElementClickInterceptedException | TimeoutException exception) {
            clickWithJavascript(locator);
        }
    }

    protected void clickWithJavascript(By locator) {
        WebElement element = prepareForInteraction(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected Object executeScript(String script, Object... arguments) {
        return ((JavascriptExecutor) driver).executeScript(script, arguments);
    }

    protected void scrollToBottom() {
        executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    protected void scrollToTop() {
        executeScript("window.scrollTo(0, 0);");
    }

    protected void scrollIntoView(By locator) {
        executeScript("arguments[0].scrollIntoView({block: 'center'});", visible(locator));
    }

    protected void waitUntilScrollAtBottom() {
        waitUtils.until(driver, config.explicitTimeout(), currentDriver -> {
            Number pageOffset = (Number) ((JavascriptExecutor) currentDriver).executeScript(
                    "return Math.ceil(window.innerHeight + window.pageYOffset);");
            Number documentHeight = (Number) ((JavascriptExecutor) currentDriver).executeScript(
                    "return Math.floor(document.body.scrollHeight);");
            return pageOffset.longValue() >= documentHeight.longValue() - 2;
        });
    }

    protected void waitUntilScrollNearTop() {
        waitUtils.until(driver, config.explicitTimeout(), currentDriver -> {
            Number pageOffset = (Number) ((JavascriptExecutor) currentDriver).executeScript("return window.pageYOffset;");
            return pageOffset.longValue() <= 5;
        });
    }

    protected void dismissBlockingInterferenceIfPresent() {
        dismissIfDisplayed(GENERIC_CLOSE_TEXT);
        dismissIfDisplayed(GENERIC_CLOSE_BUTTON);
    }

    protected boolean isElementUnobstructed(By locator) {
        try {
            return isElementUnobstructed(visible(locator));
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public void acceptConsentIfPresent() {
        List<WebElement> buttons = driver.findElements(CONSENT_ALLOW_BUTTON);
        if (buttons.isEmpty()) {
            return;
        }

        WebElement button = buttons.get(0);
        if (!button.isDisplayed()) {
            return;
        }

        try {
            button.click();
            waitUtils.untilInvisible(driver, CONSENT_ALLOW_BUTTON, Duration.ofSeconds(5));
        } catch (RuntimeException exception) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                waitUtils.untilInvisible(driver, CONSENT_ALLOW_BUTTON, Duration.ofSeconds(5));
            } catch (RuntimeException ignored) {
                // Best effort only: do not fail the scenario on consent handling.
            }
        }
    }

    private void dismissIfDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (!element.isDisplayed()) {
                continue;
            }

            try {
                element.click();
            } catch (RuntimeException exception) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                } catch (RuntimeException ignored) {
                    continue;
                }
            }

            try {
                waitUtils.untilInvisible(driver, locator, Duration.ofSeconds(2));
            } catch (RuntimeException ignored) {
                // Best effort only.
            }
            return;
        }
    }

    private WebElement prepareForInteraction(By locator) {
        WebElement element = visible(locator);
        executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
        return visible(locator);
    }

    private boolean isElementUnobstructed(WebElement element) {
        try {
            Object result = executeScript(
                    "const element = arguments[0];"
                            + "if (!element || !element.isConnected) return false;"
                            + "const rect = element.getBoundingClientRect();"
                            + "if (rect.width <= 0 || rect.height <= 0) return false;"
                            + "const x = rect.left + (rect.width / 2);"
                            + "const y = rect.top + (rect.height / 2);"
                            + "const topElement = document.elementFromPoint(x, y);"
                            + "return !!topElement && (topElement === element || element.contains(topElement));",
                    element);
            return Boolean.TRUE.equals(result);
        } catch (StaleElementReferenceException exception) {
            return false;
        }
    }

    private String normalize(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return "";
        }
        return relativePath.startsWith("/") ? relativePath : "/" + relativePath;
    }
}
