package com.lafed.taf.tests.ui;

import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.allure.AllureEnvironmentListener;
import com.lafed.taf.core.driver.DriverFactory;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.utils.ScreenshotService;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.flows.LoginInvalidUserFlow;
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
 * Executable UI test for Test Case 3: invalid login attempt.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class Tc03LoginInvalidUserTest {

    private static final Logger LOG = LoggerFactory.getLogger(Tc03LoginInvalidUserTest.class);

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

    @Test(groups = {"ui", "tc03"})
    public void shouldShowErrorForIncorrectEmailAndPassword() {
        runStep("Start test", () -> {
        });
        runStep("Launch browser", () -> {
            driverManager.start(config);
            driver = driverManager.getDriver();
        });

        new LoginInvalidUserFlow(driver, config, waitUtils).execute();
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
