package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductsPage extends BasePage {
    private static final By PRODUCTS_ROOT = By.cssSelector(".features_items");
    private static final By PRODUCTS_HEADER = By.cssSelector(".features_items h2.title.text-center");
    private static final By SEARCH_INPUT = By.cssSelector("#search_product");
    private static final By SEARCH_BUTTON = By.cssSelector("#submit_search");
    private static final By PRODUCT_CARDS = By.cssSelector(".features_items .product-image-wrapper, .features_items .single-products");
    private static final By PRODUCT_NAMES = By.cssSelector(".features_items .productinfo p");
    private static final By PRODUCT_DETAILS_LINKS = By.cssSelector(".features_items a[href*='/product_details/']");
    private static final By FIRST_VIEW_PRODUCT_LINK = By.cssSelector(".features_items a[href*='/product_details/']");
    private static final By FIRST_ADD_TO_CART_BUTTON = By.cssSelector(".features_items .productinfo a.add-to-cart");
    private static final By ADD_TO_CART_MODAL = By.cssSelector("#cartModal");
    private static final By VIEW_CART_LINK = By.cssSelector("#cartModal .modal-body a[href='/view_cart']");

    public ProductsPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public ProductsPage open() {
        openPath("/products");
        waitForDocumentReady();
        uiInterferenceGuard.afterNavigation();
        return waitUntilReady();
    }

    @Override
    public boolean isLoaded() {
        return isOnProductsPage()
                && isDisplayedSafely(PRODUCTS_ROOT)
                && isDisplayedSafely(SEARCH_INPUT)
                && isDisplayedSafely(SEARCH_BUTTON)
                && (hasVisibleProducts() || hasAnyVisible(PRODUCT_DETAILS_LINKS));
    }

    public ProductsPage waitUntilReady() {
        waitForDocumentReady();
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> isLoaded());
        return this;
    }

    public boolean hasVisibleProducts() {
        return hasAnyVisible(PRODUCT_CARDS);
    }

    public String firstVisibleProductName() {
        return findAll(PRODUCT_NAMES).stream()
                .map(element -> element.getText().trim())
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No visible product name found on the products page."));
    }

    public ProductsPage searchFor(String searchTerm) {
        waitUntilReady();
        type(SEARCH_INPUT, searchTerm);
        click(SEARCH_BUTTON);
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> isSearchResultsVisible() || currentUrl().contains("search="));
        return this;
    }

    public boolean isSearchResultsVisible() {
        return isDisplayed(PRODUCTS_HEADER)
                && textOf(PRODUCTS_HEADER).equalsIgnoreCase("Searched Products")
                && hasVisibleProducts();
    }

    public boolean containsVisibleProductNamed(String productName) {
        return findAll(PRODUCT_NAMES).stream()
                .map(element -> element.getText().trim())
                .anyMatch(name -> name.equalsIgnoreCase(productName) || name.contains(productName));
    }

    public ProductsPage addFirstVisibleProductToCart() {
        waitUntilReady();
        scrollIntoView(FIRST_ADD_TO_CART_BUTTON);
        click(FIRST_ADD_TO_CART_BUTTON);
        waitUtils.untilVisible(ADD_TO_CART_MODAL);
        return this;
    }

    public ProductDetailsPage openFirstProductDetails() {
        waitUntilReady();
        scrollIntoView(FIRST_VIEW_PRODUCT_LINK);
        click(FIRST_VIEW_PRODUCT_LINK);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver, config);
        return productDetailsPage.waitUntilReady();
    }

    public boolean isAddToCartConfirmationVisible() {
        return isDisplayed(ADD_TO_CART_MODAL);
    }

    public CartPage viewCartFromAddToCartConfirmation() {
        click(VIEW_CART_LINK);
        CartPage cartPage = new CartPage(driver, config);
        return cartPage.waitUntilReady();
    }

    private boolean isOnProductsPage() {
        return currentUrl().contains("/products") || title().contains("All Products");
    }
}
