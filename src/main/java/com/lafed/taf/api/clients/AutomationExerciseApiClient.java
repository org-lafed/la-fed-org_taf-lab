package com.lafed.taf.api.clients;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AutomationExerciseApiClient {
    private final RequestSpecification requestSpecification;

    public AutomationExerciseApiClient(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public Response get(String path, Map<String, ?> queryParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpecification);
        if (queryParams != null && !queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }
        attachRequest("GET", path, queryParams);
        Response response = request.when().get(path);
        attachResponse(response);
        return response;
    }

    public Response postForm(String path, Map<String, ?> formParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpecification).formParams(formParams);
        attachRequest("POST", path, formParams);
        Response response = request.when().post(path);
        attachResponse(response);
        return response;
    }

    public Response putForm(String path, Map<String, ?> formParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpecification).formParams(formParams);
        attachRequest("PUT", path, formParams);
        Response response = request.when().put(path);
        attachResponse(response);
        return response;
    }

    public Response deleteForm(String path, Map<String, ?> formParams) {
        RequestSpecification request = RestAssured.given().spec(requestSpecification).formParams(formParams);
        attachRequest("DELETE", path, formParams);
        Response response = request.when().delete(path);
        attachResponse(response);
        return response;
    }

    private void attachRequest(String method, String path, Map<String, ?> params) {
        String payload = method + " " + path + System.lineSeparator() + "Params: " + params;
        Allure.addAttachment("API Request", "text/plain", payload, ".txt");
    }

    private void attachResponse(Response response) {
        String payload = "Status: " + response.statusCode()
                + System.lineSeparator()
                + "Headers: " + response.getHeaders()
                + System.lineSeparator()
                + "Body: "
                + response.asPrettyString();
        Allure.addAttachment(
                "API Response",
                "application/json",
                new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)),
                ".json");
    }
}