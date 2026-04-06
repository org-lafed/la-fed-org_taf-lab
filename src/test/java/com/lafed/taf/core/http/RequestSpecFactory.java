package com.lafed.taf.core.http;

import com.lafed.taf.config.ExecutionConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Builds a minimal reusable RestAssured specification from execution config.
 */
public final class RequestSpecFactory {

    public RequestSpecification create(ExecutionConfig config) {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .addRequestSpecification(RestAssured.given())
                .build();
    }
}
