package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage extends BasePage {
    private static final By PRODUCTS_HEADER = By.cssSelector(".features_items h2.title.text-center");
    private static final By SEARCH_INPUT = By.cssSelector("#search_product");
    private static final By SEARCH_BUTTON = By.cssSelector("#submit_search");
    private static final By PRODUCT_CARDS = By.cssSelector(".features_items .product-image-wrapper");
    private static final By PRODUCT_NAMES = By.cssSelector(".features_items .productinfo p");
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
        return isDisplayed(PRODUCTS_HEADER)
                && isDisplayed(SEARCH_INPUT)
                && isDisplayed(SEARCH_BUTTON)
                && isAllProductsHeadingVisible();
    }

    public boolean isAllProductsHeadingVisible() {
        return textOf(PRODUCTS_HEADER).equalsIgnoreCase("All Products");
    }

    public boolean hasVisibleProducts() {
        return !findAll(PRODUCT_CARDS).isEmpty();
    }

    public ProductsPage searchFor(String searchTerm) {
        type(SEARCH_INPUT, searchTerm);
        click(SEARCH_BUTTON);
        waitUtils.untilUrlContains("search=");
        return this;
    }

    public boolean isSearchResultsVisible() {
        return textOf(PRODUCTS_HEADER).equalsIgnoreCase("Searched Products") && hasVisibleProducts();
    }

    public boolean containsVisibleProductNamed(String productName) {
        return findAll(PRODUCT_NAMES).stream()
                .map(element -> element.getText().trim())
                .anyMatch(name -> name.equalsIgnoreCase(productName) || name.contains(productName));
    }

    public ProductsPage addFirstVisibleProductToCart() {
        click(FIRST_ADD_TO_CART_BUTTON);
        return this;
    }
}