package com.lafed.taf.api.services;

import com.lafed.taf.api.clients.ProductsApiClient;
import com.lafed.taf.api.models.Product;
import com.lafed.taf.api.models.ProductCategory;
import com.lafed.taf.api.models.ProductUserType;
import com.lafed.taf.api.models.ProductsListResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service layer for reusable products API operations.
 */
public final class ProductsApiService extends ApiServiceSupport {

    private final ProductsApiClient productsApiClient;

    public ProductsApiService(ProductsApiClient productsApiClient) {
        super(productsApiClient);
        this.productsApiClient = Objects.requireNonNull(productsApiClient, "productsApiClient");
    }

    public Response getAllProductsListResponse() {
        return productsApiClient.getAllProductsList();
    }

    public ProductsListResponse parseProductsListResponse(Response response) {
        Objects.requireNonNull(response, "response");

        JsonPath jsonPath = new JsonPath(response.asString());
        Integer responseCode = jsonPath.getInt("responseCode");
        List<Map<String, Object>> rawProducts = jsonPath.getList("products");

        if (rawProducts == null) {
            rawProducts = Collections.emptyList();
        }

        List<Product> products = rawProducts.stream()
                .map(this::toProduct)
                .toList();

        return new ProductsListResponse(responseCode, products);
    }

    private Product toProduct(Map<String, Object> rawProduct) {
        Map<String, Object> categoryMap = toMap(rawProduct.get("category"));
        Map<String, Object> userTypeMap = categoryMap == null ? null : toMap(categoryMap.get("usertype"));

        ProductCategory category = categoryMap == null
                ? null
                : new ProductCategory(
                        userTypeMap == null ? null : new ProductUserType(asString(userTypeMap.get("usertype"))),
                        asString(categoryMap.get("category")));

        return new Product(
                toInteger(rawProduct.get("id")),
                asString(rawProduct.get("name")),
                asString(rawProduct.get("price")),
                asString(rawProduct.get("brand")),
                category);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object rawValue) {
        if (rawValue instanceof Map<?, ?> rawMap) {
            return (Map<String, Object>) rawMap;
        }
        return null;
    }

    private Integer toInteger(Object rawValue) {
        if (rawValue instanceof Number number) {
            return number.intValue();
        }

        if (rawValue instanceof String text && !text.isBlank()) {
            return Integer.parseInt(text);
        }

        return null;
    }

    private String asString(Object rawValue) {
        return rawValue == null ? null : String.valueOf(rawValue);
    }
}
