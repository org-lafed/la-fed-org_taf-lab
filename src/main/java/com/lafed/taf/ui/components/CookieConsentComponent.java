package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.BasePage;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CookieConsentComponent extends BasePage {
    private static final List<By> KNOWN_ACCEPT_BUTTONS = List.of(
            By.cssSelector("button.fc-cta-consent"),
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
        KNOWN_ACCEPT_BUTTONS.stream()
                .filter(this::isDisplayedSafely)
                .findFirst()
                .ifPresent(this::click);
    }

    private boolean isDisplayedSafely(By locator) {
        return !driver.findElements(locator).isEmpty() && driver.findElement(locator).isDisplayed();
    }
}
