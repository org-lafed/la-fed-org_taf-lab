package com.lafed.taf.core.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lafed.taf.config.ExecutionConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;

/**
 * Provides a shared RestAssured request specification.
 */
public final class ApiClientFactory {
    private ApiClientFactory() {
    }

    public static RequestSpecification create(ExecutionConfig config) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RestAssured.registerParser("text/html", Parser.JSON);

        return new RequestSpecBuilder()
                .setBaseUri(config.getApiBaseUrl())
                .setRelaxedHTTPSValidation()
                .setConfig(RestAssuredConfig.config().objectMapperConfig(
                        new ObjectMapperConfig().jackson2ObjectMapperFactory((type, charset) -> objectMapper)))
                .build();
    }
}
