package com.lafed.taf.core.listeners;

import io.qameta.allure.Allure;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Small helper utilities around Allure runtime output.
 */
public final class AllureSupport {
    private static final Path RESULTS_DIRECTORY = Path.of("target", "allure-results");

    private AllureSupport() {
    }

    public static void initialiseResultsDirectory() {
        try {
            Files.createDirectories(RESULTS_DIRECTORY);
            copyResourceIfPresent("categories.json", RESULTS_DIRECTORY.resolve("categories.json"));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to initialize Allure results directory", exception);
        }
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content, ".txt");
    }

    public static String timestampedLabel(String prefix) {
        return prefix + "-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }

    private static void copyResourceIfPresent(String resourceName, Path target) throws IOException {
        try (InputStream inputStream = AllureSupport.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
