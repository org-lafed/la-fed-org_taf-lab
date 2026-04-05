package com.lafed.taf.api.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.models.ProductsResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Map;

public class ProductApiService {
    private static final String PRODUCTS_PATH = "/api/productsList";
    private static final String SEARCH_PRODUCTS_PATH = "/api/searchProduct";

    private final AutomationExerciseApiClient apiClient;
    private final ObjectMapper objectMapper;

    public ProductApiService(AutomationExerciseApiClient apiClient) {
        this.apiClient = apiClient;
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Response getAllProductsResponse() {
        return apiClient.get(PRODUCTS_PATH, Map.of());
    }

    public ProductsResponse getAllProducts() {
        return parseProducts(getAllProductsResponse());
    }

    public ProductsResponse parseProducts(Response response) {
        try {
            return objectMapper.readValue(response.getBody().asString(), ProductsResponse.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to deserialize products response body.", exception);
        }
    }

    public Response searchProducts(String searchTerm) {
        return apiClient.postForm(SEARCH_PRODUCTS_PATH, Map.of("search_product", searchTerm));
    }
}