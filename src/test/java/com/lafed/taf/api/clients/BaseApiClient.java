package com.lafed.taf.api.clients;

import io.restassured.specification.RequestSpecification;
import java.util.Objects;

/**
 * Shared base wrapper for future API clients.
 */
public class BaseApiClient {

    private final RequestSpecification requestSpecification;

    public BaseApiClient(RequestSpecification requestSpecification) {
        this.requestSpecification = Objects.requireNonNull(requestSpecification, "requestSpecification");
    }

    protected RequestSpecification request() {
        return requestSpecification;
    }
}
