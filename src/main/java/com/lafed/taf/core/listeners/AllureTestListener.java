package com.lafed.taf.core.listeners;

import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.utils.ScreenshotService;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Adds framework-specific reporting hooks on top of Allure TestNG.
 */
public class AllureTestListener implements ITestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureTestListener.class);

    @Override
    public void onStart(ITestContext context) {
        AllureSupport.initialiseResultsDirectory();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (ConfigManager.getConfig().isScreenshotOnFailure()) {
            try {
                WebDriver driver = DriverManager.getDriver();
                ScreenshotService.attachScreenshot(driver, AllureSupport.timestampedLabel(result.getName()));
            } catch (IllegalStateException exception) {
                LOGGER.debug("No active driver available for screenshot capture: {}", exception.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getThrowable() != null) {
            AllureSupport.attachText("Skip Reason", result.getThrowable().getMessage());
        }
    }
}
