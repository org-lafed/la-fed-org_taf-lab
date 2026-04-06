package com.lafed.taf.core.allure;

import com.lafed.taf.config.ExecutionConfig;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AllureEnvironmentWriter {

    private static final String RESULTS_DIR_PROPERTY = "allure.results.directory";
    private static final Path DEFAULT_RESULTS_DIR = Paths.get("target", "allure-results");

    private AllureEnvironmentWriter() {
    }

    public static void write(ExecutionConfig config) {
        Path target = Paths.get(System.getProperty(RESULTS_DIR_PROPERTY, DEFAULT_RESULTS_DIR.toString()));
        try {
            Files.createDirectories(target);
            Path propertiesFile = target.resolve("environment.properties");

            Map<String, String> metadata = new LinkedHashMap<>();
            metadata.put("os.name", System.getProperty("os.name", ""));
            metadata.put("os.version", System.getProperty("os.version", ""));
            metadata.put("os.arch", System.getProperty("os.arch", ""));
            metadata.put("java.version", System.getProperty("java.version", ""));
            metadata.put("browser", config.browserName());
            metadata.put("env", config.envName());

            try (BufferedWriter writer = Files.newBufferedWriter(propertiesFile, StandardCharsets.UTF_8)) {
                for (Map.Entry<String, String> entry : metadata.entrySet()) {
                    writer.write(entry.getKey());
                    writer.write('=');
                    writer.write(entry.getValue());
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create Allure environment metadata", exception);
        }
    }
}
