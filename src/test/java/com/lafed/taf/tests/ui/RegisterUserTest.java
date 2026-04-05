package com.lafed.taf.tests.ui;

import com.lafed.taf.api.models.User;
import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.data.builders.UserDataBuilder;
import com.lafed.taf.data.generators.FakeDataGenerator;
import com.lafed.taf.ui.flows.SignupFlow;
import com.lafed.taf.ui.pages.AccountCreatedPage;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import org.testng.annotations.Test;

public class RegisterUserTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Register a new user through the stable signup happy path.")
    public void shouldRegisterNewUser() {
        String suffix = FakeDataGenerator.timestamp();
        User user = UserDataBuilder.aUser()
                .withName("TAF Demo " + suffix)
                .withPassword("UiDemoPass123!")
                .build();

        try {
            HomePage homePage = new HomePage(driver, config).open();
            homePage.cookieConsent().acceptIfPresent();
            UiAssertions.assertTrue(homePage.isLoaded(), "Home page should be loaded before signup starts.");

            LoginPage loginPage = homePage.openLoginPage();
            loginPage.cookieConsent().acceptIfPresent();
            UiAssertions.assertTrue(loginPage.isLoaded(), "Login page should expose both login and signup forms.");

            AccountCreatedPage accountCreatedPage = new SignupFlow(loginPage).register(user);
            UiAssertions.assertTrue(accountCreatedPage.isLoaded(), "Account created confirmation page should be displayed.");
            UiAssertions.assertTrue(
                    accountCreatedPage.normalizedConfirmationMessage().contains("account created"),
                    "Account creation confirmation should be visible.");

            HomePage loggedInHomePage = accountCreatedPage.continueToHomePage();
            loggedInHomePage.cookieConsent().acceptIfPresent();
            loggedInHomePage.waitUntilReady();
            UiAssertions.assertTrue(loggedInHomePage.isLoaded(), "User should return to the home page after account creation.");
            UiAssertions.assertTrue(
                    loggedInHomePage.isLoggedInAs(user.getName()),
                    "Header should display the logged-in user marker after registration.");
        } finally {
            deleteUserQuietly(user);
        }
    }
}
