package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HeaderComponent extends BasePage {
    private static final By NAVIGATION_CONTAINER = By.cssSelector("header .shop-menu ul");
    private static final By HOME_LINK = By.cssSelector("header .shop-menu a[href='/']");
    private static final By PRODUCTS_LINK = By.cssSelector("header .shop-menu a[href='/products']");
    private static final By CART_LINK = By.cssSelector("header .shop-menu a[href='/view_cart']");
    private static final By LOGIN_LINK = By.cssSelector("header .shop-menu a[href='/login']");

    public HeaderComponent(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(NAVIGATION_CONTAINER);
    }

    public boolean hasCoreNavigationLinks() {
        return isDisplayed(HOME_LINK)
                && isDisplayed(PRODUCTS_LINK)
                && isDisplayed(CART_LINK)
                && isDisplayed(LOGIN_LINK);
    }

    public void openLogin() {
        click(LOGIN_LINK);
    }

    public void openProducts() {
        click(PRODUCTS_LINK);
    }

    public void openCart() {
        click(CART_LINK);
    }
}
