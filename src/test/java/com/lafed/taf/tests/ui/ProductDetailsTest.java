package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.ui.pages.ProductDetailsPage;
import com.lafed.taf.ui.pages.ProductsPage;
import org.testng.annotations.Test;

public class ProductDetailsTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Open the first product details page and verify the core product information is visible.")
    public void shouldOpenProductDetailsForVisibleProduct() {
        ProductsPage productsPage = new ProductsPage(driver, config).open();
        productsPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(productsPage.isLoaded(), "Products page should be loaded.");
        UiAssertions.assertTrue(productsPage.hasVisibleProducts(), "Products list should show at least one visible product.");

        String expectedProductName = productsPage.firstVisibleProductName();
        ProductDetailsPage productDetailsPage = productsPage.openFirstProductDetails();
        productDetailsPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(productDetailsPage.isLoaded(), "Product details page should expose the core product information.");
        UiAssertions.assertContains(productDetailsPage.title(), "Product Details", "Page title should reflect the product details page.");
        UiAssertions.assertContains(productDetailsPage.productName(), expectedProductName, "Product details should match the selected product.");
        UiAssertions.assertTrue(productDetailsPage.isProductImageVisible(), "Product image should be visible.");
        UiAssertions.assertTrue(productDetailsPage.categoryText() != null, "Product category should be visible.");
        UiAssertions.assertTrue(productDetailsPage.hasAvailabilityInformation(), "Availability information should be visible.");
        UiAssertions.assertTrue(productDetailsPage.hasConditionInformation(), "Condition information should be visible.");
        UiAssertions.assertTrue(productDetailsPage.hasBrandInformation(), "Brand information should be visible.");
    }
}
