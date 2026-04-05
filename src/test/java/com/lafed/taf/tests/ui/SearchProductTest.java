package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.ui.pages.ProductsPage;
import org.testng.annotations.Test;

public class SearchProductTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Search for a known product and verify the filtered results are visible.")
    public void shouldSearchForKnownProduct() {
        ProductsPage productsPage = new ProductsPage(driver, config).open();
        productsPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(productsPage.isLoaded(), "Products page should be loaded.");
        UiAssertions.assertTrue(productsPage.hasVisibleProducts(), "Products list should show at least one visible product.");

        productsPage.searchFor("Blue Top");

        UiAssertions.assertTrue(productsPage.isSearchResultsVisible(), "Searched products heading and results should be visible.");
        UiAssertions.assertTrue(
                productsPage.containsVisibleProductNamed("Blue Top"),
                "Filtered results should contain the searched product name.");
    }
}