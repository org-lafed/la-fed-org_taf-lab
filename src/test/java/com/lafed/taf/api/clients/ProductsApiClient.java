package com.lafed.taf.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Reusable client for products API endpoints.
 */
public final class ProductsApiClient extends BaseApiClient {

    private static final String PRODUCTS_API_BASE_URL = "https://automationexercise.com";
    private static final String PRODUCTS_LIST_PATH = "/api/productsList";

    public ProductsApiClient(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public Response getAllProductsList() {
        return RestAssured.given()
                .spec(request())
                .baseUri(PRODUCTS_API_BASE_URL)
                .when()
                .get(PRODUCTS_LIST_PATH);
    }
}
