package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetailsPage extends BasePage {
    private static final By PRODUCT_IMAGE = By.cssSelector(".view-product img");
    private static final By PRODUCT_INFORMATION = By.cssSelector(".product-information");
    private static final By PRODUCT_NAME = By.cssSelector(".product-information h2");
    private static final By PRODUCT_PRICE = By.cssSelector(".product-information span span");
    private static final By PRODUCT_CATEGORY = By.cssSelector(".product-information p");
    private static final By QUANTITY_INPUT = By.cssSelector(".product-information #quantity");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector(".product-information button.cart");

    public ProductDetailsPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        return isOnProductDetailsPage()
                && isDisplayedSafely(PRODUCT_IMAGE)
                && isDisplayedSafely(PRODUCT_INFORMATION)
                && isDisplayedSafely(PRODUCT_NAME)
                && isDisplayedSafely(PRODUCT_PRICE)
                && isDisplayedSafely(QUANTITY_INPUT)
                && isDisplayedSafely(ADD_TO_CART_BUTTON);
    }

    public ProductDetailsPage waitUntilReady() {
        waitForDocumentReady();
        new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                .until(ignored -> isLoaded());
        return this;
    }

    public boolean isProductImageVisible() {
        return isDisplayed(PRODUCT_IMAGE);
    }

    public String productName() {
        return textOf(PRODUCT_NAME);
    }

    public String priceText() {
        return textOf(PRODUCT_PRICE);
    }

    public String categoryText() {
        return findDetailLine("Category:");
    }

    public boolean hasAvailabilityInformation() {
        return findDetailLine("Availability:") != null;
    }

    public boolean hasConditionInformation() {
        return findDetailLine("Condition:") != null;
    }

    public boolean hasBrandInformation() {
        return findDetailLine("Brand:") != null;
    }

    private String findDetailLine(String prefix) {
        List<String> detailLines = findAll(PRODUCT_CATEGORY).stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isBlank())
                .toList();

        return detailLines.stream()
                .filter(line -> line.startsWith(prefix))
                .findFirst()
                .orElse(null);
    }

    private boolean isOnProductDetailsPage() {
        return currentUrl().contains("/product_details/") || title().contains("Product Details");
    }
}
