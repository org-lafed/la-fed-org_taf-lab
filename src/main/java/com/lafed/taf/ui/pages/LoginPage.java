package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By LOGIN_EMAIL_INPUT = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD_INPUT = By.cssSelector("input[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");
    private static final By SIGNUP_NAME_INPUT = By.cssSelector("input[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL_INPUT = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");

    public LoginPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    public LoginPage open() {
        openPath("/login");
        return this;
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(LOGIN_EMAIL_INPUT) && isDisplayed(SIGNUP_NAME_INPUT);
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
        click(LOGIN_BUTTON);
        return new HomePage(driver, config);
    }

    public LoginPage enterSignupName(String name) {
        type(SIGNUP_NAME_INPUT, name);
        return this;
    }

    public LoginPage enterSignupEmail(String email) {
        type(SIGNUP_EMAIL_INPUT, email);
        return this;
    }

    public SignupPage startSignup() {
        click(SIGNUP_BUTTON);
        return new SignupPage(driver, config);
    }
}
