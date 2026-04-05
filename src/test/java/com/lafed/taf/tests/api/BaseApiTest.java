package com.lafed.taf.tests.api;

import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.services.ProductApiService;
import com.lafed.taf.api.services.UserApiService;
import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.http.ApiClientFactory;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {
    protected ExecutionConfig config;
    protected RequestSpecification requestSpecification;
    protected AutomationExerciseApiClient apiClient;
    protected ProductApiService productApiService;
    protected UserApiService userApiService;

    @BeforeClass(alwaysRun = true)
    public void setUpApiTest() {
        config = ConfigManager.getConfig();
        requestSpecification = ApiClientFactory.create(config);
        apiClient = new AutomationExerciseApiClient(requestSpecification);
        productApiService = new ProductApiService(apiClient);
        userApiService = new UserApiService(apiClient);
    }
}
