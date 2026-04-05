package com.lafed.taf.api.services;

import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.models.ApiResponse;
import com.lafed.taf.api.models.User;
import io.restassured.response.Response;
import java.util.Map;

public class UserApiService {
    private static final String CREATE_ACCOUNT_PATH = "/api/createAccount";
    private static final String DELETE_ACCOUNT_PATH = "/api/deleteAccount";
    private static final String GET_ACCOUNT_DETAILS_PATH = "/api/getUserDetailByEmail";

    private final AutomationExerciseApiClient apiClient;

    public UserApiService(AutomationExerciseApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Response createUserResponse(User user) {
        return apiClient.postForm(CREATE_ACCOUNT_PATH, user.toFormParams());
    }

    public ApiResponse createUser(User user) {
        return createUserResponse(user).as(ApiResponse.class);
    }

    public Response deleteUserResponse(String email, String password) {
        return apiClient.deleteForm(DELETE_ACCOUNT_PATH, Map.of("email", email, "password", password));
    }

    public ApiResponse deleteUser(String email, String password) {
        return deleteUserResponse(email, password).as(ApiResponse.class);
    }

    public Response getUserByEmailResponse(String email) {
        return apiClient.get(GET_ACCOUNT_DETAILS_PATH, Map.of("email", email));
    }
}
