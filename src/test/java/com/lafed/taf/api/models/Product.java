package com.lafed.taf.api.models;

/**
 * Minimal product contract used by API 1 assertions.
 */
public record Product(Integer id, String name, String price, String brand, ProductCategory category) {
}
