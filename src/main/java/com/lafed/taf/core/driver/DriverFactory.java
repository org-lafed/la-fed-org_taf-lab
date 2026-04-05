package com.lafed.taf.core.driver;

import com.lafed.taf.config.ExecutionConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
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
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
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
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (config.isHeadless()) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(ExecutionConfig config) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }
}