package com.lafed.taf.tests.ui;

import com.lafed.taf.assertions.UiAssertions;
import com.lafed.taf.core.utils.ResourceUtils;
import com.lafed.taf.data.generators.FakeDataGenerator;
import com.lafed.taf.ui.pages.ContactUsPage;
import com.lafed.taf.ui.pages.HomePage;
import org.testng.annotations.Test;

public class ContactUsUploadTest extends BaseUiTest {
    @Test(groups = {"ui"}, description = "Submit the contact us form with a file upload and verify the success confirmation.")
    public void shouldSubmitContactUsFormWithUpload() {
        ContactUsPage contactUsPage = new ContactUsPage(driver, config).open();
        contactUsPage.cookieConsent().acceptIfPresent();

        UiAssertions.assertTrue(contactUsPage.isLoaded(), "Contact us page should be loaded before filling the form.");

        String suffix = FakeDataGenerator.timestamp();
        contactUsPage
                .enterName("TAF Contact " + suffix)
                .enterEmail("taf-contact-" + suffix + "@example.com")
                .enterSubject("Contact upload validation")
                .enterMessage("This contact message validates the upload-enabled contact flow.")
                .uploadFile(ResourceUtils.getResourcePath("testdata/ui/contact-upload.txt").toAbsolutePath().toString())
                .submitSuccessfully();

        UiAssertions.assertContains(
                contactUsPage.successMessage(),
                "Success! Your details have been submitted successfully.",
                "Contact us submission should show a success confirmation.");

        HomePage homePage = contactUsPage.openHomePage();
        homePage.cookieConsent().acceptIfPresent();
        UiAssertions.assertTrue(homePage.isLoaded(), "Home page should be reachable from the contact us success state.");
    }
}
