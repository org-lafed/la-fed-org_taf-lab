package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the "ACCOUNT CREATED!" confirmation.
 */
public final class AccountCreatedPage extends BasePage {

    private static final By CONFIRMATION_HEADING = By.xpath(
            "//b[normalize-space()='Account Created!' or normalize-space()='ACCOUNT CREATED!']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountCreatedPage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public AccountCreatedPage assertVisible() {
        visible(CONFIRMATION_HEADING);
        visible(CONTINUE_BUTTON);
        return this;
    }

    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver, config, waitUtils);
    }
}