package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import com.lafed.taf.ui.guards.UiInterferenceGuard;
import java.util.function.Supplier;
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
    private final UiInterferenceGuard interferenceGuard;

    public SmokeLoginLogoutFlow(WebDriver driver, ExecutionConfig config, com.lafed.taf.core.utils.WaitUtils waitUtils) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.interferenceGuard = new UiInterferenceGuard(driver, config, waitUtils);
    }

    public void execute() {
        LOG.info("[INFO] Browser={} | BaseUrl={}", config.browserName(), config.uiBaseUrl());

        HomePage homePage = protectedStep("Navigate to target site", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        step("Scroll down to bottom of page", homePage::scrollToBottomOfPage);
        step("Scroll up to hero/menu section", homePage::scrollBackToHero);

        LoginPage loginPage = protectedStep("Click signup/login", homePage::openSignupLogin);
        step("Enter login email", () -> loginPage.enterLoginEmail(config.smokeLoginEmail()));
        step("Enter login password", () -> loginPage.enterLoginPassword(config.smokeLoginPassword()));

        HomePage authenticatedHomePage = protectedStep("Click login button", loginPage::submitLogin);
        protectedAssertionStep(
                "Verify login success",
                () -> authenticatedHomePage.waitUntilAuthenticated(config.smokeLoginDisplayName()),
                loginPage::submitLogin);

        LoginPage loggedOutLoginPage = protectedStep("Click logout button", authenticatedHomePage::logout);
        protectedAssertionStep("Verify logout success", loggedOutLoginPage::waitUntilReady, authenticatedHomePage::logout);
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

    private <T> T protectedStep(String action, Supplier<T> supplier) {
        return step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                T result = supplier.get();
                interferenceGuard.finalizeProtectedStep();
                return result;
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                T result = supplier.get();
                interferenceGuard.finalizeProtectedStep();
                return result;
            }
        });
    }

    private void protectedAssertionStep(String action, Runnable assertion, Runnable transitionRetry) {
        step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                assertion.run();
                interferenceGuard.finalizeProtectedStep();
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                transitionRetry.run();
                interferenceGuard.finalizeProtectedStep();
                assertion.run();
                interferenceGuard.finalizeProtectedStep();
            }
        });
    }
}
