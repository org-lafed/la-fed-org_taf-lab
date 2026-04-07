package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Minimal header/navigation wrapper for the smoke scenario.
 */
public final class HeaderComponent extends BasePage {

    private static final By SIGNUP_LOGIN_LINK = By.cssSelector("header a[href='/login']");
    private static final By LOGOUT_LINK = By.cssSelector("header a[href='/logout']");
    private static final By LOGGED_IN_MARKER = By.xpath("//header//a[contains(normalize-space(.), 'Logged in as')]");

    public HeaderComponent(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public void waitUntilAnonymousNavigationReady() {
        visible(SIGNUP_LOGIN_LINK);
    }

    public boolean isAnonymousNavigationReady() {
        return isDisplayed(SIGNUP_LOGIN_LINK);
    }

    public void clickSignupLogin() {
        scrollIntoView(SIGNUP_LOGIN_LINK);
        click(SIGNUP_LOGIN_LINK);
    }

    public void waitUntilAuthenticated(String expectedDisplayName) {
        visible(LOGGED_IN_MARKER);
        visible(LOGOUT_LINK);
        if (!expectedDisplayName.isBlank() && !text(LOGGED_IN_MARKER).contains(expectedDisplayName)) {
            throw new IllegalStateException("Authenticated header marker does not contain expected display name.");
        }
    }

    public boolean isLoggedInAs(String expectedDisplayName) {
        return isDisplayed(LOGGED_IN_MARKER) && text(LOGGED_IN_MARKER).contains(expectedDisplayName);
    }

    public void clickLogout() {
        scrollIntoView(LOGOUT_LINK);
        click(LOGOUT_LINK);
    }
}
