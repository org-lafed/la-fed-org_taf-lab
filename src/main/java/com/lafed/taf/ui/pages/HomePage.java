package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.components.CookieConsentComponent;
import com.lafed.taf.ui.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private static final By HERO_SECTION = By.cssSelector("#slider");

    public HomePage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public HomePage open() {
        openPath("/");
        waitUtils.untilTitleContains("Automation Exercise");
        return this;
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