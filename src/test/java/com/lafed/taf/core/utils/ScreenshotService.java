package com.lafed.taf.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic screenshot utility for future UI diagnostics.
 */
public final class ScreenshotService {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenshotService.class);

    public Optional<Path> capture(WebDriver driver, Path target) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return Optional.empty();
        }

        try {
            if (target.getParent() != null) {
                Files.createDirectories(target.getParent());
            }
            Files.write(target, screenshotDriver.getScreenshotAs(OutputType.BYTES));
            return Optional.of(target);
        } catch (IOException | RuntimeException exception) {
            LOG.warn("[WARN] Unable to capture screenshot at {}. Continuing teardown.", target, exception);
            return Optional.empty();
        }
    }
}
