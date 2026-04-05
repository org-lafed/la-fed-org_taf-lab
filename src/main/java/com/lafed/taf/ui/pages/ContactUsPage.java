package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactUsPage extends BasePage {
    private static final By CONTACT_US_HEADING = By.cssSelector(".contact-form h2.title.text-center");
    private static final By NAME_INPUT = By.cssSelector("input[data-qa='name']");
    private static final By EMAIL_INPUT = By.cssSelector("input[data-qa='email']");
    private static final By SUBJECT_INPUT = By.cssSelector("input[data-qa='subject']");
    private static final By MESSAGE_INPUT = By.cssSelector("textarea[data-qa='message']");
    private static final By FILE_UPLOAD_INPUT = By.cssSelector("input[name='upload_file']");
    private static final By SUBMIT_BUTTON = By.cssSelector("input[data-qa='submit-button']");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".status.alert.alert-success");
    private static final By HOME_BUTTON = By.cssSelector("#form-section a.btn.btn-success");

    public ContactUsPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public ContactUsPage open() {
        openPath("/contact_us");
        waitUtils.untilTitleContains("Contact Us");
        return this;
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(CONTACT_US_HEADING)
                && isDisplayed(NAME_INPUT)
                && isDisplayed(EMAIL_INPUT)
                && isDisplayed(SUBJECT_INPUT)
                && isDisplayed(MESSAGE_INPUT)
                && isDisplayed(FILE_UPLOAD_INPUT)
                && isDisplayed(SUBMIT_BUTTON);
    }

    public ContactUsPage enterName(String name) {
        type(NAME_INPUT, name);
        return this;
    }

    public ContactUsPage enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    public ContactUsPage enterSubject(String subject) {
        type(SUBJECT_INPUT, subject);
        return this;
    }

    public ContactUsPage enterMessage(String message) {
        type(MESSAGE_INPUT, message);
        return this;
    }

    public ContactUsPage uploadFile(String filePath) {
        find(FILE_UPLOAD_INPUT).sendKeys(filePath);
        return this;
    }

    public ContactUsPage submitSuccessfully() {
        scrollIntoView(SUBMIT_BUTTON);
        click(SUBMIT_BUTTON);
        acceptAlert();
        waitUtils.untilVisible(SUCCESS_MESSAGE);
        return this;
    }

    public String successMessage() {
        return textOf(SUCCESS_MESSAGE);
    }

    public HomePage openHomePage() {
        click(HOME_BUTTON);
        HomePage homePage = new HomePage(driver, config).waitUntilReady();
        return homePage;
    }

    private void acceptAlert() {
        try {
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitTimeoutSeconds()))
                    .until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Expected contact form confirmation alert was not displayed.", exception);
        }
    }
}
