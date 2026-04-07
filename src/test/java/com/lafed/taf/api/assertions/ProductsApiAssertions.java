package com.lafed.taf.api.assertions;

import com.lafed.taf.api.models.Product;
import com.lafed.taf.api.models.ProductCategory;
import com.lafed.taf.api.models.ProductUserType;
import com.lafed.taf.api.models.ProductsListResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Assertions dedicated to the products list API contract.
 */
public final class ProductsApiAssertions {

    private ProductsApiAssertions() {
    }

    public static void assertBodyIsValidJson(Response response) {
        Assert.assertNotNull(response, "Response must not be null.");

        String body = response.asString();
        Assert.assertFalse(body.isBlank(), "Response body must not be blank.");

        JsonPath jsonPath = new JsonPath(body);
        Assert.assertNotNull(jsonPath.getMap("$"), "Response body must be a valid JSON object.");
    }

    public static void assertApplicationResponseCode(ProductsListResponse payload, int expectedResponseCode) {
        Assert.assertNotNull(payload, "Parsed payload must not be null.");
        Assert.assertEquals(payload.responseCode(), expectedResponseCode, "Unexpected API payload responseCode.");
    }

    public static void assertContainsUsableProducts(ProductsListResponse payload) {
        Assert.assertNotNull(payload, "Parsed payload must not be null.");
        Assert.assertNotNull(payload.products(), "Products collection must not be null.");
        Assert.assertFalse(payload.products().isEmpty(), "Products collection must not be empty.");

        for (Product product : payload.products()) {
            assertProductIsUsable(product);
        }
    }

    private static void assertProductIsUsable(Product product) {
        Assert.assertNotNull(product, "Product item must not be null.");
        Assert.assertNotNull(product.id(), "Product id must not be null.");
        Assert.assertTrue(isNotBlank(product.name()), "Product name must not be blank.");
        Assert.assertTrue(isNotBlank(product.price()), "Product price must not be blank.");

        ProductCategory category = product.category();
        Assert.assertNotNull(category, "Product category must not be null.");
        Assert.assertTrue(isNotBlank(category.category()), "Product category name must not be blank.");

        ProductUserType userType = category.usertype();
        Assert.assertNotNull(userType, "Product category user type must not be null.");
        Assert.assertTrue(isNotBlank(userType.usertype()), "Product category user type label must not be blank.");
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
