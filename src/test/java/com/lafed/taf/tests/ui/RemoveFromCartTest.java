package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.ui.flows.CartFlow;
import com.lafed.taf.ui.pages.CartPage;
import com.lafed.taf.ui.pages.ProductsPage;
import org.testng.annotations.Test;

public class RemoveFromCartTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Add a visible product to cart, remove it, and verify the empty cart state.")
    public void shouldRemoveProductFromCart() {
        ProductsPage productsPage = new ProductsPage(driver, config).open();
        productsPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(productsPage.isLoaded(), "Products page should be loaded.");
        UiAssertions.assertTrue(productsPage.hasVisibleProducts(), "Products list should show at least one visible product.");

        String expectedProductName = productsPage.firstVisibleProductName();
        CartPage cartPage = new CartFlow(productsPage).addFirstVisibleProductAndOpenCart();
        cartPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(cartPage.isLoaded(), "Cart page should be loaded after opening the add-to-cart confirmation.");
        UiAssertions.assertTrue(cartPage.hasProductsInCart(), "Cart should contain the added product.");
        UiAssertions.assertTrue(cartPage.containsProductNamed(expectedProductName), "Cart should contain the selected product.");

        cartPage.removeFirstProduct();

        UiAssertions.assertTrue(cartPage.isEmpty(), "Cart should be empty after removing the only product.");
        UiAssertions.assertContains(cartPage.emptyCartMessage(), "Cart is empty!", "Empty cart message should be visible after removal.");
    }
}
