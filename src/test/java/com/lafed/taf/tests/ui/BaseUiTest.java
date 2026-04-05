package com.lafed.taf.tests.ui;

import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.models.ApiResponse;
import com.lafed.taf.api.models.User;
import com.lafed.taf.api.services.UserApiService;
import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.http.ApiClientFactory;
import com.lafed.taf.core.state.StateStore;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {
    protected ExecutionConfig config;
    protected WebDriver driver;
    protected RequestSpecification requestSpecification;
    protected AutomationExerciseApiClient apiClient;
    protected UserApiService userApiService;

    @BeforeMethod(alwaysRun = true)
    public void setUpUiTest() {
        config = ConfigManager.getConfig();
        driver = DriverManager.createDriver(config);
        requestSpecification = ApiClientFactory.create(config);
        apiClient = new AutomationExerciseApiClient(requestSpecification);
        userApiService = new UserApiService(apiClient);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownUiTest() {
        StateStore.clear();
        DriverManager.quitDriver();
    }

    protected void deleteUserQuietly(User user) {
        try {
            ApiResponse response = userApiService.deleteUser(user.getEmail(), user.getPassword());
            if (response.getResponseCode() != 200 && response.getResponseCode() != 404) {
                throw new IllegalStateException("Unexpected delete response code: " + response.getResponseCode());
            }
        } catch (RuntimeException ignored) {
            // Keep teardown best-effort for demo stability.
        }
    }
}