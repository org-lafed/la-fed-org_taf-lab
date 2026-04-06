package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates a local Chrome or Firefox driver using Selenium Manager.
 */
public final class DriverFactory {

    public WebDriver createDriver(ExecutionConfig config) {
        WebDriver driver = switch (config.browserName().trim().toLowerCase()) {
            case "chrome" -> createChromeDriver(config);
            case "firefox" -> createFirefoxDriver(config);
            default -> throw new IllegalArgumentException("Unsupported browser: " + config.browserName());
        };

        driver.manage().timeouts().pageLoadTimeout(config.pageLoadTimeout());
        driver.manage().window().setSize(new Dimension(1440, 900));
        return driver;
    }

    private WebDriver createChromeDriver(ExecutionConfig config) {
        ChromeOptions options = new ChromeOptions();
        if (config.headless()) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=1440,900");
        if (!config.browserBinaryPath().isBlank()) {
            options.setBinary(config.browserBinaryPath());
        }
        Path driverPath = findDriverExecutable("chromedriver.exe");
        if (driverPath != null) {
            System.setProperty("webdriver.chrome.driver", driverPath.toString());
        }
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver(ExecutionConfig config) {
        FirefoxOptions options = new FirefoxOptions();
        if (config.headless()) {
            options.addArguments("-headless");
        }
        if (!config.browserBinaryPath().isBlank()) {
            options.setBinary(config.browserBinaryPath());
        }
        Path driverPath = findDriverExecutable("geckodriver.exe");
        if (driverPath != null) {
            System.setProperty("webdriver.gecko.driver", driverPath.toString());
        }
        return new FirefoxDriver(options);
    }

    private Path findDriverExecutable(String executableName) {
        Path driverRoot = Paths.get(System.getProperty("user.dir"), ".wdm");
        if (!Files.exists(driverRoot)) {
            return null;
        }

        try (Stream<Path> paths = Files.walk(driverRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equalsIgnoreCase(executableName))
                    .max(Comparator.comparing(Path::toString))
                    .orElse(null);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to resolve local driver executable: " + executableName, exception);
        }
    }
}
