package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Home page interactions needed by the smoke login/logout flow.
 */
public final class HomePage extends BasePage {

    private static final By HERO_SECTION = By.id("slider");
    private static final By SUBSCRIPTION_HEADING = By.xpath("//footer//h2[normalize-space()='Subscription']");

    public HomePage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public HomePage open() {
        open("/");
        return waitUntilReady();
    }

    public HomePage waitUntilReady() {
        waitUtils.untilTitleContains(driver, "Automation Exercise", config.explicitTimeout());
        header().waitUntilAnonymousNavigationReady();
        visible(HERO_SECTION);
        return this;
    }

    public HomePage scrollToBottomOfPage() {
        scrollIntoView(SUBSCRIPTION_HEADING);
        visible(SUBSCRIPTION_HEADING);
        return this;
    }

    public HomePage scrollBackToHero() {
        scrollToTop();
        visible(HERO_SECTION);
        waitUntilScrollNearTop();
        return this;
    }

    public LoginPage openSignupLogin() {
        header().clickSignupLogin();
        return new LoginPage(driver, config, waitUtils).waitUntilReady();
    }

    public HomePage waitUntilAuthenticated(String expectedDisplayName) {
        waitUtils.untilTitleContains(driver, "Automation Exercise", config.explicitTimeout());
        header().waitUntilAuthenticated(expectedDisplayName);
        visible(HERO_SECTION);
        return this;
    }

    public boolean isLoggedInAs(String expectedDisplayName) {
        return header().isLoggedInAs(expectedDisplayName);
    }

    public LoginPage logout() {
        header().clickLogout();
        return new LoginPage(driver, config, waitUtils).waitUntilReady();
    }

    public AccountDeletedPage deleteAccount() {
        header().clickDeleteAccount();
        return new AccountDeletedPage(driver, config, waitUtils);
    }

    private HeaderComponent header() {
        return new HeaderComponent(driver, config, waitUtils);
    }
}
