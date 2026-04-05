package com.lafed.taf.core.utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Captures browser screenshots and attaches them to Allure.
 */
public final class ScreenshotService {
    private ScreenshotService() {
    }

    public static void attachScreenshot(WebDriver driver, String attachmentName) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(attachmentName, "image/png", new ByteArrayInputStream(screenshot), ".png");
    }
}
