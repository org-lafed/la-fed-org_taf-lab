package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.guards.UiInterferenceGuard;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates Test Case 3: login with invalid credentials.
 */
public final class LoginInvalidUserFlow {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInvalidUserFlow.class);

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final WaitUtils waitUtils;
    private final UiInterferenceGuard interferenceGuard;

    public LoginInvalidUserFlow(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.interferenceGuard = new UiInterferenceGuard(driver, config, waitUtils);
    }

    public void execute(String invalidEmail, String invalidPassword) {
        LOG.info("[INFO] Executing TC03 invalid login for email={}", invalidEmail);

        HomePage homePage = protectedStep("Navigate to url 'http://automationexercise.com'", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        step("Verify that home page is visible successfully", homePage::assertVisible);

        LoginPage loginPage = protectedStep("Click on 'Signup / Login' button", homePage::openSignupLogin);
        step("Verify 'Login to your account' is visible", loginPage::assertLoginToYourAccountVisible);
        step("Enter incorrect email address and password", () -> loginPage
                .enterLoginEmail(invalidEmail)
                .enterLoginPassword(invalidPassword));

        protectedStep("Click 'login' button", loginPage::submitLogin);
        step("Verify error 'Your email or password is incorrect!' is visible",
                loginPage::assertInvalidCredentialsErrorVisible);
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
}
