package com.lafed.taf.tests.ui;

import com.lafed.taf.api.models.ApiResponse;
import com.lafed.taf.api.models.User;
import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.data.builders.UserDataBuilder;
import com.lafed.taf.data.generators.FakeDataGenerator;
import com.lafed.taf.ui.flows.LoginFlow;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidLoginTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Login with a valid existing user and verify the logged-in marker.")
    public void shouldLoginWithValidCredentials() {
        String suffix = FakeDataGenerator.timestamp();
        User user = UserDataBuilder.aUser()
                .withName("TAF Login " + suffix)
                .withPassword("UiDemoPass123!")
                .build();

        try {
            ApiResponse createResponse = userApiService.createUser(user);
            Assert.assertEquals(createResponse.getResponseCode(), 201, "API setup should create the test user.");

            LoginPage loginPage = new LoginPage(driver, config).open();
            loginPage.cookieConsent().acceptIfPresent();
            UiAssertions.assertTrue(loginPage.isLoaded(), "Login page should be loaded before credentials are submitted.");

            HomePage homePage = new LoginFlow(loginPage).login(user.getEmail(), user.getPassword());
            homePage.cookieConsent().acceptIfPresent();
            UiAssertions.assertTrue(homePage.isLoaded(), "Home page should be visible after successful login.");
            UiAssertions.assertTrue(homePage.isLoggedInAs(user.getName()), "Header should show the authenticated user name.");
        } finally {
            deleteUserQuietly(user);
        }
    }
}