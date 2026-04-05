package com.lafed.taf.tests.ui;

import com.lafed.taf.config.ConfigManager;
import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.driver.DriverManager;
import com.lafed.taf.core.state.StateStore;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {
    protected ExecutionConfig config;
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUpUiTest() {
        config = ConfigManager.getConfig();
        driver = DriverManager.createDriver(config);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownUiTest() {
        StateStore.clear();
        DriverManager.quitDriver();
    }
}
