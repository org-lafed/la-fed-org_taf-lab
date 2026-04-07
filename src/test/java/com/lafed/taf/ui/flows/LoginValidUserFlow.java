package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.data.generators.UserAccountData;
import com.lafed.taf.ui.guards.UiInterferenceGuard;
import com.lafed.taf.ui.pages.AccountDeletedPage;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates Test Case 2: login with a valid user and delete the account.
 */
public final class LoginValidUserFlow {

    private static final Logger LOG = LoggerFactory.getLogger(LoginValidUserFlow.class);

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final WaitUtils waitUtils;
    private final UiInterferenceGuard interferenceGuard;
    private final UserAccountData user;

    public LoginValidUserFlow(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils, UserAccountData user) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.interferenceGuard = new UiInterferenceGuard(driver, config, waitUtils);
        this.user = user;
    }

    public void execute() {
        LOG.info("[INFO] Executing TC02 valid login for email={}", user.email());

        HomePage homePage = protectedStep("Navigate to url 'http://automationexercise.com'", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        step("Verify that home page is visible successfully", homePage::assertVisible);

        LoginPage loginPage = protectedStep("Click on 'Signup / Login' button", homePage::openSignupLogin);
        step("Verify 'Login to your account' is visible", loginPage::assertLoginToYourAccountVisible);
        step("Enter correct email address and password", () -> loginPage
                .enterLoginEmail(user.email())
                .enterLoginPassword(user.password()));

        HomePage authenticatedHomePage = protectedStep("Click 'login' button", loginPage::submitLogin);
        protectedAssertionStep(
                "Verify that 'Logged in as username' is visible",
                () -> authenticatedHomePage.waitUntilAuthenticated(user.displayName()),
                loginPage::submitLogin);

        LOG.info("[INFO] Deleting account for email={}", user.email());
        AccountDeletedPage accountDeletedPage = protectedStep(
                "Click 'Delete Account' button",
                authenticatedHomePage::deleteAccount);
        protectedAssertionStep(
                "Verify that 'ACCOUNT DELETED!' is visible",
                accountDeletedPage::assertVisible,
                authenticatedHomePage::deleteAccount);
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

    private <T> T step(String action, Supplier<T> supplier) {
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
