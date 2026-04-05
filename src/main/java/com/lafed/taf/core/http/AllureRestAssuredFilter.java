package com.lafed.taf.core.http;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Attaches API request and response details to Allure.
 */
public class AllureRestAssuredFilter implements Filter {
    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext context) {
        attachRequest(requestSpec);
        Response response = context.next(requestSpec, responseSpec);
        attachResponse(response);
        return response;
    }

    private void attachRequest(FilterableRequestSpecification requestSpec) {
        StringBuilder builder = new StringBuilder();
        builder.append(requestSpec.getMethod())
                .append(" ")
                .append(requestSpec.getURI())
                .append(System.lineSeparator());

        if (!requestSpec.getHeaders().asList().isEmpty()) {
            builder.append("Headers: ").append(requestSpec.getHeaders()).append(System.lineSeparator());
        }
        if (!requestSpec.getFormParams().isEmpty()) {
            builder.append("Form params: ").append(requestSpec.getFormParams()).append(System.lineSeparator());
        }
        if (requestSpec.getBody() != null) {
            builder.append("Body: ").append(String.valueOf(requestSpec.getBody())).append(System.lineSeparator());
        }

        Allure.addAttachment("API Request", "text/plain", builder.toString(), ".txt");
    }

    private void attachResponse(Response response) {
        String body = "Status: " + response.statusCode()
                + System.lineSeparator()
                + "Headers: " + response.getHeaders()
                + System.lineSeparator()
                + "Body: "
                + response.asPrettyString();

        Allure.addAttachment(
                "API Response",
                "application/json",
                new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)),
                ".json");
    }
}
