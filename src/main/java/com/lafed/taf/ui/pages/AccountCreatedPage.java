package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    public HomePage continueToHomePage() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver, config);
    }
}