package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.ui.pages.HomePage;
import org.testng.annotations.Test;

public class HomePageSmokeTest extends BaseUiTest {
    @Test(groups = {"smoke", "ui"}, description = "Verify that the Automation Exercise home page loads with core navigation.")
    public void shouldLoadHomePageAndDisplayCoreNavigation() {
        HomePage homePage = new HomePage(driver, config).open();
        homePage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(homePage.isLoaded(), "Home page should be loaded.");
        UiAssertions.assertTrue(homePage.header().hasCoreNavigationLinks(), "Header should expose the core navigation links.");
        UiAssertions.assertTrue(homePage.isHeroBannerVisible(), "Hero banner should be visible.");
        UiAssertions.assertContains(homePage.title(), "Automation Exercise", "Page title should contain the site name.");
    }
}
