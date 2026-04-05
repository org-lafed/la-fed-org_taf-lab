package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.components.CookieConsentComponent;
import com.lafed.taf.ui.components.HeaderComponent;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {
    private static final By HERO_SECTION = By.cssSelector("#slider");

    public HomePage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public HomePage open() {
        openPath("/");
        return waitUntilReady();
    }

    @Override
    public boolean isLoaded() {
        HeaderComponent header = header();
        return header.isLogoVisible()
                && header.isLoaded()
                && isDisplayed(HERO_SECTION);
    }

    public boolean isHeroBannerVisible() {
        return isDisplayed(HERO_SECTION);
    }

    public boolean hasCoreMarkers() {
        HeaderComponent header = header();
        return header.isLogoVisible() && isDisplayed(HERO_SECTION);
    }

    public HomePage waitUntilReady() {
        waitUtils.untilTitleContains("Automation Exercise");
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> hasCoreMarkers());
        return this;
    }

    public boolean isLoggedInAs(String userName) {
        return header().isLoggedInAs(userName);
    }

    public HeaderComponent header() {
        return new HeaderComponent(driver, config);
    }

    public CookieConsentComponent cookieConsent() {
        return new CookieConsentComponent(driver, config);
    }

    public LoginPage openLoginPage() {
        header().openLogin();
        return new LoginPage(driver, config);
    }

    public ProductsPage openProductsPage() {
        header().openProducts();
        return new ProductsPage(driver, config);
    }
}
