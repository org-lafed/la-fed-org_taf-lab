package com.lafed.taf.tests.ui;

<<<<<<< HEAD
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
=======
import com.lafed.taf.core.allure.AllureEnvironmentListener;
import com.lafed.taf.core.utils.ScreenshotService;
import com.lafed.taf.ui.flows.LoginInvalidUserFlow;
import io.qameta.allure.testng.AllureTestNg;
import java.nio.file.Path;
import java.util.UUID;
>>>>>>> origin/main
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
<<<<<<< HEAD
 * Executable UI test for Test Case 3: invalid login attempt.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class Tc03LoginInvalidUserTest {

    private static final Logger LOG = LoggerFactory.getLogger(Tc03LoginInvalidUserTest.class);

    private ExecutionConfig config;
    private DriverManager driverManager;
    private WaitUtils waitUtils;
=======
 * Executes Test Case 3: Login User with incorrect email and password.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class Tc03LoginInvalidUserTest extends BaseUiTest {

    private static final Logger LOG = LoggerFactory.getLogger(Tc03LoginInvalidUserTest.class);

>>>>>>> origin/main
    private ScreenshotService screenshotService;
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
<<<<<<< HEAD
        this.config = ConfigManager.load();
        this.driverManager = new DriverManager(new DriverFactory());
        this.waitUtils = new WaitUtils();
=======
        initializeUiContext();
>>>>>>> origin/main
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

<<<<<<< HEAD
    @Test(groups = {"ui", "tc03"})
    public void shouldShowErrorForIncorrectEmailAndPassword() {
=======
    @Test(groups = {"ui", "tc03-login-invalid-user"})
    public void shouldShowErrorWhenCredentialsAreInvalid() {
>>>>>>> origin/main
        runStep("Start test", () -> {
        });
        runStep("Launch browser", () -> {
            driverManager.start(config);
            driver = driverManager.getDriver();
        });

<<<<<<< HEAD
        new LoginInvalidUserFlow(driver, config, waitUtils).execute();
=======
        String invalidEmail = "invalid-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String invalidPassword = "wrong-password";
        new LoginInvalidUserFlow(driver, config, waitUtils).execute(invalidEmail, invalidPassword);
>>>>>>> origin/main
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
