package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountCreatedPage extends BasePage {
    private static final By ACCOUNT_CREATED_HEADER = By.cssSelector("h2[data-qa='account-created']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountCreatedPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(ACCOUNT_CREATED_HEADER);
    }

    public String confirmationMessage() {
        return textOf(ACCOUNT_CREATED_HEADER);
    }

    public String normalizedConfirmationMessage() {
        return confirmationMessage()
                .replaceAll("[^A-Za-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    public HomePage continueToHomePage() {
        click(CONTINUE_BUTTON);
        HomePage homePage = new HomePage(driver, config);
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> homePage.hasCoreMarkers());
        return homePage;
    }
}
