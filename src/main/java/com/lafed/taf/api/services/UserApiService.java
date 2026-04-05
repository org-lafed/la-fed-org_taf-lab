package com.lafed.taf.api.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.models.ApiResponse;
import com.lafed.taf.api.models.User;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Map;

public class UserApiService {
    private static final String CREATE_ACCOUNT_PATH = "/api/createAccount";
    private static final String DELETE_ACCOUNT_PATH = "/api/deleteAccount";
    private static final String GET_ACCOUNT_DETAILS_PATH = "/api/getUserDetailByEmail";

    private final AutomationExerciseApiClient apiClient;
    private final ObjectMapper objectMapper;

    public UserApiService(AutomationExerciseApiClient apiClient) {
        this.apiClient = apiClient;
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Response createUserResponse(User user) {
        return apiClient.postForm(CREATE_ACCOUNT_PATH, user.toFormParams());
    }

    public ApiResponse createUser(User user) {
        return parseApiResponse(createUserResponse(user));
    }

    public Response deleteUserResponse(String email, String password) {
        return apiClient.deleteForm(DELETE_ACCOUNT_PATH, Map.of("email", email, "password", password));
    }

    public ApiResponse deleteUser(String email, String password) {
        return parseApiResponse(deleteUserResponse(email, password));
    }

    public Response getUserByEmailResponse(String email) {
        return apiClient.get(GET_ACCOUNT_DETAILS_PATH, Map.of("email", email));
    }

    public ApiResponse parseApiResponse(Response response) {
        try {
            return objectMapper.readValue(response.getBody().asString(), ApiResponse.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to deserialize user API response body.", exception);
        }
    }
}