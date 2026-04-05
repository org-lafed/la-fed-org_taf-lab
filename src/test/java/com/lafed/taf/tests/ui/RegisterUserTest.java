package com.lafed.taf.tests.ui;

import com.lafed.taf.data.builders.UserDataBuilder;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class RegisterUserTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Starter registration scenario kept compile-safe until live locator validation is finished.")
    public void shouldRegisterNewUser() {
        UserDataBuilder.aUser().build();
        throw new SkipException("TODO: locator validation is still required for the full signup/account creation journey on automationexercise.com.");
    }
}
