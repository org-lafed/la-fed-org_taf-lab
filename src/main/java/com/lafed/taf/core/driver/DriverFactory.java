package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates browser instances based on the active execution configuration.
 */
public final class DriverFactory {
    private static final List<String> MACOS_CHROME_BINARY_CANDIDATES = List.of(
            "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
            "/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing"
    );
    private static final Path WEBDRIVER_CACHE_DIRECTORY = Path.of(".wdm").toAbsolutePath().normalize();

    private DriverFactory() {
    }

    public static WebDriver createDriver(ExecutionConfig config) {
        WebDriver driver = switch (config.getBrowser().toLowerCase()) {
            case "chrome" -> createChromeDriver(config);
            case "firefox" -> createFirefoxDriver(config);
            case "edge" -> createEdgeDriver(config);
            default -> throw new IllegalArgumentException("Unsupported browser: " + config.getBrowser());
        };

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeoutSeconds()));
        driver.manage().window().setSize(new Dimension(config.getWindowWidth(), config.getWindowHeight()));
        return driver;
    }

    private static WebDriver createChromeDriver(ExecutionConfig config) {
        webDriverManager(WebDriverManager.chromedriver()).setup();
        ChromeOptions options = new ChromeOptions();
        String browserBinaryPath = resolveChromeBinaryPath(config);
        if (browserBinaryPath != null) {
            options.setBinary(browserBinaryPath);
        }
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(ExecutionConfig config) {
        webDriverManager(WebDriverManager.firefoxdriver()).setup();
        FirefoxOptions options = new FirefoxOptions();
        if (config.isHeadless()) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(ExecutionConfig config) {
        EdgeOptions options = new EdgeOptions();
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }

    private static WebDriverManager webDriverManager(WebDriverManager manager) {
        ensureWebDriverCacheDirectoryExists();
        String cacheDirectory = WEBDRIVER_CACHE_DIRECTORY.toString();
        return manager
                .cachePath(cacheDirectory)
                .resolutionCachePath(cacheDirectory);
    }

    private static void ensureWebDriverCacheDirectoryExists() {
        try {
            Files.createDirectories(WEBDRIVER_CACHE_DIRECTORY);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create WebDriverManager cache directory: "
                    + WEBDRIVER_CACHE_DIRECTORY, exception);
        }
    }

    private static String resolveChromeBinaryPath(ExecutionConfig config) {
        if (config.getBrowserBinaryPath() != null) {
            return config.getBrowserBinaryPath();
        }

        if (!isMacOs()) {
            return null;
        }

        return MACOS_CHROME_BINARY_CANDIDATES.stream()
                .filter(candidate -> Files.isRegularFile(Path.of(candidate)))
                .findFirst()
                .orElse(null);
    }

    private static boolean isMacOs() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("mac");
    }
}
