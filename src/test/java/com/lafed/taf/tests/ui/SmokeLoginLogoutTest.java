package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.driver.DriverFactory;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.utils.ScreenshotService;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Single real smoke suite for home scroll, login, and logout.
 */
public final class SmokeLoginLogoutTest {

    private ExecutionConfig config;
    private DriverManager driverManager;
    private WaitUtils waitUtils;
    private ScreenshotService screenshotService;
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        this.config = ConfigManager.load();
        this.driverManager = new DriverManager(new DriverFactory());
        this.waitUtils = new WaitUtils();
        this.screenshotService = new ScreenshotService();

        driverManager.start(config);
        this.driver = driverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && config.screenshotOnFailure() && !result.isSuccess()) {
                String fileName = result.getName() + "-" + config.browserName() + ".png";
                screenshotService.capture(driver, Path.of("target", "screenshots", fileName));
            }
        } finally {
            driverManager.stop();
        }
    }

    @Test(groups = {"smoke", "ui"})
    public void shouldLoginAndLogoutFromTheHomePage() {
        HomePage homePage = new HomePage(driver, config, waitUtils).open();
        homePage.acceptConsentIfPresent();
        homePage.scrollToBottomOfPage();
        homePage.scrollBackToHero();

        LoginPage loginPage = homePage.openSignupLogin();
        UiAssertions.assertTrue(loginPage.isReady(), "The login page should be ready before credentials are submitted.");

        HomePage authenticatedHomePage = loginPage
                .login(config.smokeLoginEmail(), config.smokeLoginPassword())
                .waitUntilAuthenticated(config.smokeLoginDisplayName());

        UiAssertions.assertTrue(authenticatedHomePage.isLoggedInAs(config.smokeLoginDisplayName()),
                "The authenticated header marker should be visible after login.");

        LoginPage loggedOutLoginPage = authenticatedHomePage.logout();
        UiAssertions.assertTrue(loggedOutLoginPage.isReady(),
                "The login page should be visible again after logout.");
    }
}
