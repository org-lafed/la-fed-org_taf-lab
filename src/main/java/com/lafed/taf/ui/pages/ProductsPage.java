package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {
    private static final By PRODUCTS_HEADER = By.cssSelector(".features_items h2.title");
    private static final By FIRST_ADD_TO_CART_BUTTON = By.cssSelector(".features_items .product-image-wrapper:first-of-type .productinfo a.add-to-cart");

    public ProductsPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public ProductsPage open() {
        openPath("/products");
        return this;
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(PRODUCTS_HEADER);
    }

    public ProductsPage addFirstVisibleProductToCart() {
        click(FIRST_ADD_TO_CART_BUTTON);
        return this;
    }
}
