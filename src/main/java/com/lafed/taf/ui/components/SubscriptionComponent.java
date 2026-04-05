package com.lafed.taf.ui.components;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.ui.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SubscriptionComponent extends BasePage {
    private static final By FOOTER = By.cssSelector("footer");
    private static final By SUBSCRIPTION_TITLE = By.cssSelector("footer h2");
    private static final By EMAIL_INPUT = By.cssSelector("#susbscribe_email");
    private static final By SUBMIT_BUTTON = By.cssSelector("#subscribe");
    private static final By SUCCESS_MESSAGE = By.cssSelector("#success-subscribe");

    public SubscriptionComponent(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        scrollIntoView(FOOTER);
        return isDisplayed(EMAIL_INPUT)
                && isDisplayed(SUBMIT_BUTTON)
                && (!driver.findElements(SUBSCRIPTION_TITLE).isEmpty()
                ? textOf(SUBSCRIPTION_TITLE).equalsIgnoreCase("Subscription")
                : true);
    }

    public SubscriptionComponent subscribe(String email) {
        scrollIntoView(FOOTER);
        type(EMAIL_INPUT, email);
        click(SUBMIT_BUTTON);
        waitUtils.untilVisible(SUCCESS_MESSAGE);
        return this;
    }

    public String successMessage() {
        return textOf(SUCCESS_MESSAGE);
    }
}
