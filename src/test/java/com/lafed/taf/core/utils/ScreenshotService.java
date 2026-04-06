package com.lafed.taf.core.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Generic screenshot utility for future UI diagnostics.
 */
public final class ScreenshotService {

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
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to persist screenshot: " + target, exception);
        }
    }
}
