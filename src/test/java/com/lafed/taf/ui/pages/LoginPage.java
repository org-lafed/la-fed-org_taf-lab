package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Login and signup page interactions used by UI scenarios.
 */
public final class LoginPage extends BasePage {

    private static final By LOGIN_HEADING = By.xpath("//h2[normalize-space()='Login to your account']");
    private static final By NEW_USER_SIGNUP_HEADING = By.xpath("//h2[normalize-space()='New User Signup!']");
    private static final By LOGIN_EMAIL_INPUT = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD_INPUT = By.cssSelector("input[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");
    private static final By SIGNUP_NAME_INPUT = By.cssSelector("input[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL_INPUT = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");

    public LoginPage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public LoginPage waitUntilReady() {
        waitUtils.untilUrlContains(driver, "/login", config.explicitTimeout());
        visible(LOGIN_HEADING);
        visible(LOGIN_EMAIL_INPUT);
        visible(LOGIN_PASSWORD_INPUT);
        visible(LOGIN_BUTTON);
        return this;
    }

    public boolean isReady() {
        return isDisplayed(LOGIN_HEADING)
                && isDisplayed(LOGIN_EMAIL_INPUT)
                && isDisplayed(LOGIN_PASSWORD_INPUT)
                && isDisplayed(LOGIN_BUTTON);
    }

    public LoginPage enterLoginEmail(String email) {
        type(LOGIN_EMAIL_INPUT, email);
        return this;
    }

    public LoginPage enterLoginPassword(String password) {
        type(LOGIN_PASSWORD_INPUT, password);
        return this;
    }

    public HomePage submitLogin() {
        scrollIntoView(LOGIN_BUTTON);
        click(LOGIN_BUTTON);
        return new HomePage(driver, config, waitUtils);
    }

    public HomePage login(String email, String password) {
        return enterLoginEmail(email)
                .enterLoginPassword(password)
                .submitLogin();
    }

    public LoginPage assertNewUserSignupVisible() {
        visible(NEW_USER_SIGNUP_HEADING);
        visible(SIGNUP_NAME_INPUT);
        visible(SIGNUP_EMAIL_INPUT);
        visible(SIGNUP_BUTTON);
        return this;
    }

    public LoginPage enterSignupName(String name) {
        type(SIGNUP_NAME_INPUT, name);
        return this;
    }

    public LoginPage enterSignupEmail(String email) {
        type(SIGNUP_EMAIL_INPUT, email);
        return this;
    }

    public AccountInformationPage submitNewUser() {
        scrollIntoView(SIGNUP_BUTTON);
        click(SIGNUP_BUTTON);
        return new AccountInformationPage(driver, config, waitUtils);
    }
}