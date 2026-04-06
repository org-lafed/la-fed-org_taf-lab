package com.lafed.taf.tests.api;

import com.lafed.taf.api.clients.BaseApiClient;
import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.http.RequestSpecFactory;
import com.lafed.taf.core.state.StateStore;

/**
 * Shared API test scaffold.
 */
public abstract class BaseApiTest {

    protected ExecutionConfig config;
    protected RequestSpecFactory requestSpecFactory;
    protected StateStore stateStore;

    protected void initializeApiContext() {
        this.config = ConfigManager.load();
        this.requestSpecFactory = new RequestSpecFactory();
        this.stateStore = new StateStore();
    }

    protected BaseApiClient newApiClient() {
        return new BaseApiClient(requestSpecFactory.create(config));
    }
}
