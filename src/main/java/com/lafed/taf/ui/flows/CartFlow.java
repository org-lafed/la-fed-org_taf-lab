package com.lafed.taf.ui.flows;

import com.lafed.taf.ui.pages.CartPage;
import com.lafed.taf.ui.pages.ProductsPage;

public class CartFlow {
    private final ProductsPage productsPage;

    public CartFlow(ProductsPage productsPage) {
        this.productsPage = productsPage;
    }

    public CartPage addFirstVisibleProductAndOpenCart() {
        productsPage.addFirstVisibleProductToCart();
        return productsPage.viewCartFromAddToCartConfirmation();
    }
}
