package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.BasePage;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

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
        return KNOWN_ACCEPT_BUTTONS.stream().anyMatch(this::isDisplayedSafely);
    }

    public void acceptIfPresent() {
        try {
            KNOWN_ACCEPT_BUTTONS.stream()
                    .filter(this::isDisplayedSafely)
                    .findFirst()
                    .ifPresent(locator -> {
                        click(locator);
                        waitForBannerToDisappear(locator);
                    });
        } catch (StaleElementReferenceException | ElementClickInterceptedException ignored) {
            // Tolerate ephemeral cookie banners that disappear before interaction.
        }
    }

    private void waitForBannerToDisappear(By locator) {
        try {
            waitUtils.untilInvisible(locator);
        } catch (RuntimeException ignored) {
            // Keep tests moving if the consent layer closes asynchronously.
        }
    }

    private boolean isDisplayedSafely(By locator) {
        return !driver.findElements(locator).isEmpty() && driver.findElement(locator).isDisplayed();
    }
}