package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dedicated smoke flow with step-oriented execution logging.
 */
public final class SmokeLoginLogoutFlow {

    private static final Logger LOG = LoggerFactory.getLogger(SmokeLoginLogoutFlow.class);

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final com.lafed.taf.core.utils.WaitUtils waitUtils;

    public SmokeLoginLogoutFlow(WebDriver driver, ExecutionConfig config, com.lafed.taf.core.utils.WaitUtils waitUtils) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
    }

    public void execute() {
        LOG.info("[INFO] Browser={} | BaseUrl={}", config.browserName(), config.uiBaseUrl());

        HomePage homePage = step("Navigate to target site", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        step("Scroll down to bottom of page", homePage::scrollToBottomOfPage);
        step("Scroll up to hero/menu section", homePage::scrollBackToHero);

        LoginPage loginPage = step("Click signup/login", homePage::openSignupLogin);
        step("Enter login email", () -> loginPage.enterLoginEmail(config.smokeLoginEmail()));
        step("Enter login password", () -> loginPage.enterLoginPassword(config.smokeLoginPassword()));

        HomePage authenticatedHomePage = step("Click login button", loginPage::submitLogin);
        step("Verify login success", () -> authenticatedHomePage.waitUntilAuthenticated(config.smokeLoginDisplayName()));

        LoginPage loggedOutLoginPage = step("Click logout button", authenticatedHomePage::logout);
        step("Verify logout success", loggedOutLoginPage::waitUntilReady);
    }

    private void step(String action, Runnable runnable) {
        LOG.info("[START] {}", action);
        try {
            runnable.run();
            LOG.info("[DONE] {}", action);
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }

    private <T> T step(String action, java.util.function.Supplier<T> supplier) {
        LOG.info("[START] {}", action);
        try {
            T result = supplier.get();
            LOG.info("[DONE] {}", action);
            return result;
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }
}
