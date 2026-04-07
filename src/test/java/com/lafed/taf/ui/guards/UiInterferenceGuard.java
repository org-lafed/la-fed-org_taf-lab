package com.lafed.taf.ui.guards;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import java.net.URI;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeps the AUT window in focus and recovers from blocking UI interferences.
 */
public final class UiInterferenceGuard {

    private static final Logger LOG = LoggerFactory.getLogger(UiInterferenceGuard.class);

    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(2);
    private static final List<By> CLOSE_ACTION_CANDIDATES = List.of(
            By.xpath(
                    "//*[normalize-space()='Close'"
                            + " or normalize-space()='Dismiss'"
                            + " or normalize-space()='Skip'"
                            + " or normalize-space()='Skip Ad'"
                            + " or normalize-space()='Skip Ads'"
                            + " or normalize-space()='No Thanks'"
                            + " or normalize-space()='No thanks'"
                            + " or normalize-space()='Not now'"
                            + " or normalize-space()='X'"
                            + " or normalize-space()='x'"
                            + " or normalize-space()='\u00D7']"),
            By.xpath(
                    "//*[self::button or self::a or @role='button']"
                            + "[normalize-space()='Close'"
                            + " or normalize-space()='Dismiss'"
                            + " or normalize-space()='Skip'"
                            + " or normalize-space()='Skip Ad'"
                            + " or normalize-space()='Skip Ads'"
                            + " or normalize-space()='No Thanks'"
                            + " or normalize-space()='No thanks'"
                            + " or normalize-space()='Not now'"
                            + " or normalize-space()='X'"
                            + " or normalize-space()='x'"
                            + " or normalize-space()='\u00D7']"),
            By.cssSelector(
                    "[aria-label='Close'], [aria-label='Dismiss'], [title='Close'], [title='Dismiss'],"
                            + " [data-dismiss], .close, .btn-close"));

    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private final String autHost;

    private String autWindowHandle;
    private String lastKnownAutUrl;

    public UiInterferenceGuard(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this.driver = Objects.requireNonNull(driver, "driver");
        this.waitUtils = Objects.requireNonNull(waitUtils, "waitUtils");
        Objects.requireNonNull(config, "config");
        this.autHost = URI.create(config.uiBaseUrl()).getHost();
    }

    public void prepareProtectedStep() {
        captureAutWindow();
        restoreAutWindowContext();
    }

    public void finalizeProtectedStep() {
        captureAutWindow();
        restoreAutWindowContext();
        dismissBlockingOverlayIfPresent();
    }

    public void recoverAfterInterference() {
        restoreAutWindowContext();
        dismissBlockingOverlayIfPresent();
    }

    public boolean isProbableInterference(RuntimeException exception) {
        if (exception instanceof ElementClickInterceptedException
                || exception instanceof NoSuchWindowException
                || exception instanceof TimeoutException) {
            return true;
        }

        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return false;
        }

        return message.contains("obscures it")
                || message.contains("not clickable at point")
                || message.contains("Browsing context has been discarded")
                || message.contains("no such window")
                || message.contains("target window already closed");
    }

    public void logRetry() {
        LOG.info("[INFO] Retrying protected action after interference recovery");
    }

    private void captureAutWindow() {
        if (autWindowHandle == null) {
            autWindowHandle = driver.getWindowHandle();
        }
        String currentUrl = currentUrlSafely();
        if (isAutUrl(currentUrl)) {
            lastKnownAutUrl = currentUrl;
        }
    }

    private void restoreAutWindowContext() {
        Set<String> handles = snapshotWindowHandles();
        if (handles.isEmpty()) {
            LOG.warn("[WARN] No browser window handle available while restoring AUT context");
            return;
        }

        for (String handle : handles) {
            if (handle.equals(autWindowHandle)) {
                continue;
            }

            try {
                driver.switchTo().window(handle);
                LOG.info("[INFO] Attempting to close unexpected popup window handle={}", handle);
                driver.close();
                LOG.info("[INFO] Closed unexpected popup window handle={}", handle);
            } catch (NoSuchWindowException ignored) {
                LOG.info("[INFO] Unexpected popup window already disappeared handle={}", handle);
            } catch (WebDriverException exception) {
                LOG.warn(
                        "[WARN] Failed to close unexpected popup window handle={}; abandoning close and returning to AUT",
                        handle,
                        exception);
                switchToAutWindow(snapshotWindowHandles());
                break;
            }

            switchToAutWindow(snapshotWindowHandles());
        }

        Set<String> remainingHandles = snapshotWindowHandles();
        if (!switchToAutWindow(remainingHandles)) {
            return;
        }

        driver.switchTo().defaultContent();

        String currentUrl = currentUrlSafely();
        if (lastKnownAutUrl != null && !lastKnownAutUrl.isBlank() && !isAutUrl(currentUrl)) {
            driver.navigate().to(lastKnownAutUrl);
            currentUrl = currentUrlSafely();
        }

        if (isAutUrl(currentUrl)) {
            lastKnownAutUrl = currentUrl;
        }
    }

    private Set<String> snapshotWindowHandles() {
        try {
            return new LinkedHashSet<>(driver.getWindowHandles());
        } catch (WebDriverException exception) {
            LOG.warn("[WARN] Unable to inspect browser windows while restoring AUT context", exception);
            return Set.of();
        }
    }

    private boolean switchToAutWindow(Set<String> handles) {
        if (autWindowHandle != null && handles.contains(autWindowHandle)) {
            try {
                driver.switchTo().window(autWindowHandle);
                LOG.info("[INFO] Returned to AUT window handle={}", autWindowHandle);
                return true;
            } catch (WebDriverException exception) {
                LOG.warn("[WARN] Unable to switch back to AUT window handle={}", autWindowHandle, exception);
            }
        }

        for (String handle : handles) {
            try {
                driver.switchTo().window(handle);
                String currentUrl = currentUrlSafely();
                if (!isAutUrl(currentUrl)) {
                    continue;
                }

                autWindowHandle = handle;
                lastKnownAutUrl = currentUrl;
                LOG.info("[INFO] Returned to AUT window handle={}", autWindowHandle);
                return true;
            } catch (WebDriverException exception) {
                LOG.warn("[WARN] Unable to inspect remaining window handle={} while restoring AUT context", handle, exception);
            }
        }

        if (!handles.isEmpty()) {
            autWindowHandle = handles.iterator().next();
            try {
                driver.switchTo().window(autWindowHandle);
                LOG.warn("[WARN] AUT handle unavailable; switched to remaining window handle={}", autWindowHandle);
                return true;
            } catch (WebDriverException exception) {
                LOG.warn("[WARN] Unable to switch to remaining window handle={}", autWindowHandle, exception);
            }
        }

        LOG.warn("[WARN] Unable to switch back to AUT window because no handle remains");
        return false;
    }

    private String currentUrlSafely() {
        try {
            return driver.getCurrentUrl();
        } catch (WebDriverException exception) {
            LOG.warn("[WARN] Unable to read current browser URL while restoring AUT context", exception);
            return null;
        }
    }

    private void dismissBlockingOverlayIfPresent() {
        for (By candidateLocator : CLOSE_ACTION_CANDIDATES) {
            List<WebElement> candidates = driver.findElements(candidateLocator);
            for (WebElement candidate : candidates) {
                if (!isVisible(candidate) || !isBlockingOverlayCloseAction(candidate)) {
                    continue;
                }

                LOG.info("[INFO] Detected blocking overlay, attempting graceful close");
                tryClick(candidate);

                try {
                    waitUtils.until(driver, SHORT_TIMEOUT, currentDriver -> !isBlockingOverlayCloseAction(candidate));
                } catch (RuntimeException ignored) {
                    // Best effort only: the close action may dismiss the blocker without invalidating the button handle.
                }
                return;
            }
        }
    }

    private boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException exception) {
            return false;
        }
    }

    private boolean isBlockingOverlayCloseAction(WebElement closeAction) {
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "const closeAction = arguments[0];"
                            + "if (!closeAction || !closeAction.isConnected) return false;"
                            + "const isVisible = element => {"
                            + "  const style = window.getComputedStyle(element);"
                            + "  const rect = element.getBoundingClientRect();"
                            + "  return style.display !== 'none' && style.visibility !== 'hidden'"
                            + "    && rect.width > 0 && rect.height > 0;"
                            + "};"
                            + "if (!isVisible(closeAction)) return false;"
                            + "const selectors = 'dialog,[role=\"dialog\"],[aria-modal=\"true\"],.modal,.popup,.overlay,"
                            + "  [class*=\"modal\"],[class*=\"popup\"],[class*=\"overlay\"],[class*=\"interstitial\"],"
                            + "  [class*=\"lightbox\"],[id*=\"modal\"],[id*=\"popup\"],[id*=\"overlay\"]';"
                            + "let container = closeAction.closest(selectors);"
                            + "if (!container) {"
                            + "  let current = closeAction.parentElement;"
                            + "  while (current && current !== document.body) {"
                            + "    if (!isVisible(current)) { current = current.parentElement; continue; }"
                            + "    const style = window.getComputedStyle(current);"
                            + "    const rect = current.getBoundingClientRect();"
                            + "    const zIndex = Number.parseInt(style.zIndex || '0', 10);"
                            + "    const coversCenter = rect.left <= window.innerWidth / 2"
                            + "      && rect.right >= window.innerWidth / 2"
                            + "      && rect.top <= window.innerHeight / 2"
                            + "      && rect.bottom >= window.innerHeight / 2;"
                            + "    if (coversCenter && (style.position === 'fixed' || style.position === 'sticky' || zIndex >= 1000)) {"
                            + "      container = current;"
                            + "      break;"
                            + "    }"
                            + "    current = current.parentElement;"
                            + "  }"
                            + "}"
                            + "if (!container || !isVisible(container)) return false;"
                            + "const style = window.getComputedStyle(container);"
                            + "const rect = container.getBoundingClientRect();"
                            + "const zIndex = Number.parseInt(style.zIndex || '0', 10);"
                            + "const coversCenter = rect.left <= window.innerWidth / 2"
                            + "  && rect.right >= window.innerWidth / 2"
                            + "  && rect.top <= window.innerHeight / 2"
                            + "  && rect.bottom >= window.innerHeight / 2;"
                            + "const largeEnough = (rect.width * rect.height) >= (window.innerWidth * window.innerHeight * 0.08)"
                            + "  || rect.width >= (window.innerWidth * 0.45)"
                            + "  || rect.height >= (window.innerHeight * 0.35);"
                            + "return coversCenter && largeEnough"
                            + "  && (style.position === 'fixed' || style.position === 'sticky' || zIndex >= 1000);",
                    closeAction);
            return Boolean.TRUE.equals(result);
        } catch (StaleElementReferenceException exception) {
            return false;
        }
    }

    private void tryClick(WebElement candidate) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                    candidate);
            candidate.click();
        } catch (RuntimeException exception) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", candidate);
        }
    }

    private boolean isAutUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI current = URI.create(url);
            String currentHost = current.getHost();
            if (currentHost == null || currentHost.isBlank()) {
                return false;
            }
            return currentHost.equalsIgnoreCase(autHost) || currentHost.endsWith("." + autHost);
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
