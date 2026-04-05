package com.lafed.taf.api.services;

import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.models.ProductsResponse;
import io.restassured.response.Response;
import java.util.Map;

public class ProductApiService {
    private static final String PRODUCTS_PATH = "/api/productsList";
    private static final String SEARCH_PRODUCTS_PATH = "/api/searchProduct";

    private final AutomationExerciseApiClient apiClient;

    public ProductApiService(AutomationExerciseApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Response getAllProductsResponse() {
        return apiClient.get(PRODUCTS_PATH, Map.of());
    }

    public ProductsResponse getAllProducts() {
        return getAllProductsResponse().as(ProductsResponse.class);
    }

    public Response searchProducts(String searchTerm) {
        return apiClient.postForm(SEARCH_PRODUCTS_PATH, Map.of("search_product", searchTerm));
    }
}
