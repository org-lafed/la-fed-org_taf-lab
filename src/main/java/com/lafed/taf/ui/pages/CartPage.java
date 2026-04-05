package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage extends BasePage {
    private static final By CART_TABLE = By.cssSelector("#cart_info_table");
    private static final By CART_ROWS = By.cssSelector("#cart_info_table tbody tr");
    private static final By FIRST_PRODUCT_NAME = By.cssSelector("#cart_info_table tbody tr:first-of-type .cart_description h4 a");
    private static final By FIRST_REMOVE_BUTTON = By.cssSelector("#cart_info_table tbody tr:first-of-type .cart_quantity_delete");
    private static final By EMPTY_CART_MESSAGE = By.cssSelector("#empty_cart p.text-center");
    private static final By CHECKOUT_BUTTON = By.cssSelector(".check_out");

    public CartPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public CartPage open() {
        openPath("/view_cart");
        waitForDocumentReady();
        uiInterferenceGuard.afterNavigation();
        return waitUntilReady();
    }

    @Override
    public boolean isLoaded() {
        return isOnCartPage() && (isDisplayedSafely(CART_TABLE) || isDisplayedSafely(EMPTY_CART_MESSAGE));
    }

    public CartPage waitUntilReady() {
        waitForDocumentReady();
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> isLoaded());
        return this;
    }

    public CartPage proceedToCheckout() {
        // TODO: Validate checkout modal vs direct navigation behavior on the live cart page.
        click(CHECKOUT_BUTTON);
        return this;
    }

    public boolean hasProductsInCart() {
        return !findAll(CART_ROWS).isEmpty();
    }

    public boolean containsProductNamed(String productName) {
        return hasProductsInCart()
                && findAll(CART_ROWS).stream()
                .map(row -> row.findElement(By.cssSelector(".cart_description h4 a")).getText().trim())
                .anyMatch(name -> name.equalsIgnoreCase(productName) || name.contains(productName));
    }

    public String firstProductName() {
        return textOf(FIRST_PRODUCT_NAME);
    }

    public CartPage removeFirstProduct() {
        int initialRowCount = findAll(CART_ROWS).size();
        scrollIntoView(FIRST_REMOVE_BUTTON);
        click(FIRST_REMOVE_BUTTON);

        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ExpectedConditions.or(
                        ExpectedConditions.numberOfElementsToBeLessThan(CART_ROWS, initialRowCount),
                        ExpectedConditions.visibilityOfElementLocated(EMPTY_CART_MESSAGE)));
        return this;
    }

    public boolean isEmpty() {
        return findAll(CART_ROWS).isEmpty() && isDisplayed(EMPTY_CART_MESSAGE);
    }

    public String emptyCartMessage() {
        return textOf(EMPTY_CART_MESSAGE);
    }

    private boolean isOnCartPage() {
        return currentUrl().contains("/view_cart") || title().contains("Checkout") || title().contains("Cart");
    }
}
