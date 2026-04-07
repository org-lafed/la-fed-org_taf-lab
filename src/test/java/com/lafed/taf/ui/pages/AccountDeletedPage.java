package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the "ACCOUNT DELETED!" confirmation.
 */
public final class AccountDeletedPage extends BasePage {

    private static final By CONFIRMATION_HEADING = By.xpath(
            "//b[normalize-space()='Account Deleted!' or normalize-space()='ACCOUNT DELETED!']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountDeletedPage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public AccountDeletedPage assertVisible() {
        if (!currentUrl().contains("/delete_account")) {
            dismissBlockingInterferenceIfPresent();
            if (!currentUrl().contains("/delete_account")) {
                new HeaderComponent(driver, config, waitUtils).clickDeleteAccount();
            }
        }

        waitUtils.untilUrlContains(driver, "/delete_account", config.explicitTimeout());
        dismissBlockingInterferenceIfPresent();
        visible(CONFIRMATION_HEADING);
        visible(CONTINUE_BUTTON);
        return this;
    }

    public HomePage clickContinue() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver, config, waitUtils);
    }
}