package com.lafed.taf.ui.flows;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.data.generators.TestDataGenerator;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Dedicated UI flow for Test Case 3: invalid login.
 */
public final class LoginInvalidUserFlow {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInvalidUserFlow.class);
    private static final String INVALID_PASSWORD = "InvalidPass123!";
    private static final String INVALID_EMAIL_DOMAIN = "@example.test";

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final WaitUtils waitUtils;
    private final TestDataGenerator testDataGenerator;

    public LoginInvalidUserFlow(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this(driver, config, waitUtils, new TestDataGenerator());
    }

    LoginInvalidUserFlow(
            WebDriver driver,
            ExecutionConfig config,
            WaitUtils waitUtils,
            TestDataGenerator testDataGenerator) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.testDataGenerator = testDataGenerator;
    }

    public void execute() {
        LOG.info("[INFO] Browser={} | BaseUrl={}", config.browserName(), config.uiBaseUrl());

        String invalidEmail = buildInvalidEmail();

        HomePage homePage = step("Navigate to target site", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page.waitUntilReady();
        });
        step("Verify that home page is visible successfully",
                () -> UiAssertions.assertTrue(homePage.isReady(), "Home page must be visible."));

        LoginPage loginPage = step("Click on Signup / Login button", homePage::openSignupLogin);
        step("Verify Login to your account is visible",
                () -> UiAssertions.assertTrue(loginPage.isReady(), "'Login to your account' must be visible."));
        step("Enter incorrect email address and password", () -> loginPage
                .enterLoginEmail(invalidEmail)
                .enterLoginPassword(INVALID_PASSWORD));
        step("Click login button", loginPage::submitLoginExpectingFailure);
        step("Verify error Your email or password is incorrect! is visible", () -> {
            UiAssertions.assertTrue(loginPage.isInvalidCredentialsErrorVisible(),
                    "Invalid credentials error must be visible.");
            Assert.assertEquals(loginPage.invalidCredentialsErrorText(),
                    LoginPage.INVALID_CREDENTIALS_ERROR_TEXT,
                    "Unexpected invalid credentials error message.");
        });
    }

    private String buildInvalidEmail() {
        return testDataGenerator.token("tc03-invalid-user") + INVALID_EMAIL_DOMAIN;
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
