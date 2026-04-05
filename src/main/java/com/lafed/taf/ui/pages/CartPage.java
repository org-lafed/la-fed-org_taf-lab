package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    private static final By CART_TABLE = By.cssSelector("#cart_info_table");
    private static final By CHECKOUT_BUTTON = By.cssSelector(".check_out");

    public CartPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public CartPage open() {
        openPath("/view_cart");
        return this;
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(CART_TABLE) || currentUrl().contains("/view_cart");
    }

    public CartPage proceedToCheckout() {
        // TODO: Validate checkout modal vs direct navigation behavior on the live cart page.
        click(CHECKOUT_BUTTON);
        return this;
    }
}
