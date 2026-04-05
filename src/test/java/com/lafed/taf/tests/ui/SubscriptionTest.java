package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.data.generators.FakeDataGenerator;
import com.lafed.taf.ui.components.SubscriptionComponent;
import com.lafed.taf.ui.pages.HomePage;
import org.testng.annotations.Test;

public class SubscriptionTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Subscribe with a unique email address through the footer subscription form.")
    public void shouldSubscribeFromFooter() {
        HomePage homePage = new HomePage(driver, config).open();
        homePage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(homePage.isLoaded(), "Home page should be loaded.");

        SubscriptionComponent subscriptionComponent = homePage.subscription();
        UiAssertions.assertTrue(subscriptionComponent.isLoaded(), "Subscription form should be visible in the footer.");

        subscriptionComponent.subscribe("taf-subscription-" + FakeDataGenerator.timestamp() + "@example.com");

        UiAssertions.assertContains(
                subscriptionComponent.successMessage(),
                "You have been successfully subscribed!",
                "Subscription success message should be visible.");
    }
}
