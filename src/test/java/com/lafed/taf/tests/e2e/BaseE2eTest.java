package com.lafed.taf.tests.e2e;

import com.lafed.taf.api.clients.AutomationExerciseApiClient;
import com.lafed.taf.api.services.ProductApiService;
import com.lafed.taf.api.services.UserApiService;
import com.lafed.taf.core.http.ApiClientFactory;
import com.lafed.taf.tests.ui.BaseUiTest;

public abstract class BaseE2eTest extends BaseUiTest {
    protected AutomationExerciseApiClient apiClient;
    protected ProductApiService productApiService;
    protected UserApiService userApiService;

    @Override
    public void setUpUiTest() {
        super.setUpUiTest();
        apiClient = new AutomationExerciseApiClient(ApiClientFactory.create(config));
        productApiService = new ProductApiService(apiClient);
        userApiService = new UserApiService(apiClient);
    }
}
