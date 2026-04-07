package com.lafed.taf.tests.ui;

import com.lafed.taf.core.allure.AllureEnvironmentListener;
import com.lafed.taf.core.utils.ScreenshotService;
import com.lafed.taf.data.generators.UserAccountData;
import com.lafed.taf.ui.flows.LoginValidUserFlow;
import com.lafed.taf.ui.flows.ValidUserAccountProvisioner;
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
 * Executes Test Case 2: Login User with correct email and password.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class Tc02LoginValidUserTest extends BaseUiTest {

    private static final Logger LOG = LoggerFactory.getLogger(Tc02LoginValidUserTest.class);

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

    @Test(groups = {"ui", "tc02-login-valid-user"})
    public void shouldLoginWithAValidUserAndDeleteTheAccount() {
        runStep("Start test", () -> {
        });
        runStep("Launch browser", () -> {
            driverManager.start(config);
            driver = driverManager.getDriver();
        });

        UserAccountData validUser = new ValidUserAccountProvisioner(driver, config, waitUtils).create();
        new LoginValidUserFlow(driver, config, waitUtils, validUser).execute();
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
