package com.lafed.taf.api.assertions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import com.lafed.taf.api.models.Product;
import com.lafed.taf.api.models.ProductsResponse;
import io.restassured.response.Response;
import org.testng.Assert;

public final class ApiAssertions {
    private ApiAssertions() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Unexpected HTTP status code.");
    }

    public static void assertResponseCode(ProductsResponse response, int expectedResponseCode) {
        Assert.assertEquals(response.getResponseCode(), expectedResponseCode, "Unexpected API responseCode.");
    }

    public static void assertProductsPresent(ProductsResponse response) {
        Assert.assertNotNull(response.getProducts(), "Products collection should be present.");
        Assert.assertFalse(response.getProducts().isEmpty(), "Products collection should not be empty.");
    }

    public static void assertProductHasCoreFields(Product product) {
        Assert.assertTrue(product.getId() > 0, "Product id should be positive.");
        Assert.assertNotNull(product.getName(), "Product name should be present.");
        Assert.assertNotNull(product.getPrice(), "Product price should be present.");
        Assert.assertNotNull(product.getBrand(), "Product brand should be present.");
        Assert.assertNotNull(product.getCategory(), "Product category should be present.");
        Assert.assertNotNull(product.getCategory().getUsertype(), "Product usertype should be present.");
    }

    public static void assertMatchesSchema(Response response, String schemaPath) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
    }
}
