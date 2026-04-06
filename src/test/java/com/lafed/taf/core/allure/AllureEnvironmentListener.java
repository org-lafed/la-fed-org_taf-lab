package com.lafed.taf.core.allure;

import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import org.testng.IExecutionListener;

public final class AllureEnvironmentListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        ExecutionConfig config = ConfigManager.load();
        AllureEnvironmentWriter.write(config);
    }

    @Override
    public void onExecutionFinish() {
        // no-op
    }
}
