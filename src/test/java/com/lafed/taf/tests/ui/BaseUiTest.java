package com.lafed.taf.tests.ui;

import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.driver.DriverFactory;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.state.StateStore;
import com.lafed.taf.core.utils.WaitUtils;

/**
 * Shared UI test scaffold. Driver startup is intentionally manual and absent by default.
 */
public abstract class BaseUiTest {

    protected ExecutionConfig config;
    protected DriverManager driverManager;
    protected StateStore stateStore;
    protected WaitUtils waitUtils;

    protected void initializeUiContext() {
        this.config = ConfigManager.load();
        this.driverManager = new DriverManager(new DriverFactory());
        this.stateStore = new StateStore();
        this.waitUtils = new WaitUtils();
    }
}
