package com.lafed.taf.api.models;

import java.util.List;

/**
 * Minimal response wrapper for the products list endpoint.
 */
public record ProductsListResponse(Integer responseCode, List<Product> products) {
}
