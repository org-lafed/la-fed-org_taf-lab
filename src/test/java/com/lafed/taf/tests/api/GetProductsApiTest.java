package com.lafed.taf.tests.api;

import com.lafed.taf.api.assertions.ApiAssertions;
import com.lafed.taf.api.models.Product;
import com.lafed.taf.api.models.ProductsResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class GetProductsApiTest extends BaseApiTest {
    @Test(groups = {"api"}, description = "Verify that the public products endpoint returns a valid product catalog.")
    public void shouldReturnProductsCatalog() {
        Response response = productApiService.getAllProductsResponse();
        ProductsResponse productsResponse = response.as(ProductsResponse.class);
        Product firstProduct = productsResponse.getProducts().get(0);

        ApiAssertions.assertStatusCode(response, 200);
        ApiAssertions.assertResponseCode(productsResponse, 200);
        ApiAssertions.assertProductsPresent(productsResponse);
        ApiAssertions.assertProductHasCoreFields(firstProduct);
        ApiAssertions.assertMatchesSchema(response, "schemas/products-list-schema.json");
    }
}
