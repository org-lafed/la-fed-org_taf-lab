package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.data.generators.UserAccountData;
import com.lafed.taf.data.generators.UserAccountDataGenerator;
import com.lafed.taf.ui.guards.UiInterferenceGuard;
import com.lafed.taf.ui.pages.AccountCreatedPage;
import com.lafed.taf.ui.pages.AccountInformationPage;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a unique valid user before the TC02 login scenario.
 */
public final class ValidUserAccountProvisioner {

    private static final Logger LOG = LoggerFactory.getLogger(ValidUserAccountProvisioner.class);

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final WaitUtils waitUtils;
    private final UiInterferenceGuard interferenceGuard;
    private final UserAccountDataGenerator dataGenerator;

    public ValidUserAccountProvisioner(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.interferenceGuard = new UiInterferenceGuard(driver, config, waitUtils);
        this.dataGenerator = new UserAccountDataGenerator();
    }

    public UserAccountData create() {
        UserAccountData user = dataGenerator.generate();
        LOG.info("[INFO] Creating precondition user email={} displayName={}", user.email(), user.displayName());

        HomePage homePage = protectedStep("Precondition: navigate to target site", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        LoginPage loginPage = protectedStep("Precondition: open signup/login page", homePage::openSignupLogin);
        step("Precondition: verify signup form is visible", loginPage::assertNewUserSignupVisible);
        step("Precondition: enter unique signup name and email", () -> loginPage
                .enterSignupName(user.displayName())
                .enterSignupEmail(user.email()));
        AccountInformationPage accountInformationPage = protectedStep(
                "Precondition: submit signup",
                loginPage::submitNewUser);
        step("Precondition: verify account information form is visible", accountInformationPage::assertVisible);
        step("Precondition: fill account information", () -> accountInformationPage.fillRequiredDetails(user));
        AccountCreatedPage accountCreatedPage = protectedStep(
                "Precondition: create account",
                accountInformationPage::submitCreateAccount);
        step("Precondition: verify account created confirmation", accountCreatedPage::assertVisible);
        HomePage authenticatedHomePage = protectedStep(
                "Precondition: continue after account creation",
                accountCreatedPage::clickContinue);
        protectedAssertionStep(
                "Precondition: verify user is logged in",
                () -> authenticatedHomePage.waitUntilAuthenticated(user.displayName()),
                accountCreatedPage::clickContinue);
        LoginPage loggedOutLoginPage = protectedStep(
                "Precondition: logout newly created user",
                authenticatedHomePage::logout);
        step("Precondition: verify login page is visible again", loggedOutLoginPage::assertLoginToYourAccountVisible);
        return user;
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

    private void protectedStep(String action, Runnable runnable) {
        step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                runnable.run();
                interferenceGuard.finalizeProtectedStep();
                return null;
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                runnable.run();
                interferenceGuard.finalizeProtectedStep();
                return null;
            }
        });
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
