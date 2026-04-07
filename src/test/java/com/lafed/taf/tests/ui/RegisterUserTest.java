package com.lafed.taf.tests.ui;

import com.lafed.taf.core.allure.AllureEnvironmentListener;
import com.lafed.taf.core.utils.ScreenshotService;
import com.lafed.taf.ui.flows.RegisterUserFlow;
import io.qameta.allure.testng.AllureTestNg;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Executes Test Case 1: Register User.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class RegisterUserTest extends BaseUiTest {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserTest.class);

    private ScreenshotService screenshotService;
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        initializeUiContext();
        this.screenshotService = new ScreenshotService();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && config.screenshotOnFailure() && !result.isSuccess()) {
                screenshotService.capture(driver,
                        Path.of("target", "screenshots", result.getName() + "-" + config.browserName() + ".png"));
            }
        } finally {
            if (driver != null) {
                runStep("Close browser", () -> {
                    driverManager.stop();
                    driver = null;
                });
            }

            runStep("End test", () -> {
            });
        }
    }

    @Test(groups = {"ui", "tc01-register-user"})
    public void shouldRegisterAndDeleteUserThroughTheSignupFlow() {
        runStep("Start test", () -> {
        });
        runStep("Launch browser", () -> {
            driverManager.start(config);
            driver = driverManager.getDriver();
        });

        new RegisterUserFlow(driver, config, waitUtils).execute();
    }

    private void runStep(String action, Runnable runnable) {
        LOG.info("[START] {}", action);
        try {
            runnable.run();
            LOG.info("[DONE] {}", action);
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }
}