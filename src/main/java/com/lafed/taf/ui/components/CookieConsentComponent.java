package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.BasePage;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CookieConsentComponent extends BasePage {
    private static final By CONSENT_BUTTON = By.cssSelector("button.fc-cta-consent");
    private static final List<By> KNOWN_ACCEPT_BUTTONS = List.of(
            CONSENT_BUTTON,
            By.cssSelector("button[aria-label='Autoriser']"),
            By.cssSelector("button[aria-label='Consent']"),
            By.cssSelector("button[mode='primary']")
    );

    public CookieConsentComponent(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        return KNOWN_ACCEPT_BUTTONS.stream().anyMatch(this::hasVisibleConsentButton);
    }

    public void acceptIfPresent() {
        KNOWN_ACCEPT_BUTTONS.stream()
                .map(this::findVisibleElement)
                .filter(element -> element != null)
                .findFirst()
                .ifPresent(this::clickDirectlyIfPossible);
    }

    private boolean hasVisibleConsentButton(By locator) {
        return isDisplayedSafely(locator);
    }

    private WebElement findVisibleElement(By locator) {
        try {
            return driver.findElements(locator).stream()
                    .filter(WebElement::isDisplayed)
                    .findFirst()
                    .orElse(null);
        } catch (StaleElementReferenceException ignored) {
            return null;
        }
    }

    private void clickDirectlyIfPossible(WebElement element) {
        try {
            element.click();
        } catch (RuntimeException ignored) {
            // Keep cookie consent best-effort and non-blocking.
        }
    }
}
