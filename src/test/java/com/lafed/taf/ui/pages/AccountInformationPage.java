package com.lafed.taf.ui.pages;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Page object encapsulating the "Enter Account Information" step.
 */
public final class AccountInformationPage extends BasePage {

    private static final By HEADING = By.xpath(
            "//b[normalize-space()='Enter Account Information' or normalize-space()='ENTER ACCOUNT INFORMATION']");
    private static final By TITLE_MR = By.id("id_gender1");
    private static final By TITLE_MRS = By.id("id_gender2");
    private static final By NAME_INPUT = By.id("name");
    private static final By EMAIL_INPUT = By.id("email");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By DAYS_SELECT = By.id("days");
    private static final By MONTHS_SELECT = By.id("months");
    private static final By YEARS_SELECT = By.id("years");
    private static final By NEWSLETTER_CHECKBOX = By.id("newsletter");
    private static final By OFFERS_CHECKBOX = By.id("optin");
    private static final By FIRST_NAME_INPUT = By.id("first_name");
    private static final By LAST_NAME_INPUT = By.id("last_name");
    private static final By COMPANY_INPUT = By.id("company");
    private static final By ADDRESS_ONE_INPUT = By.id("address1");
    private static final By ADDRESS_TWO_INPUT = By.id("address2");
    private static final By COUNTRY_SELECT = By.id("country");
    private static final By STATE_INPUT = By.id("state");
    private static final By CITY_INPUT = By.id("city");
    private static final By ZIPCODE_INPUT = By.id("zipcode");
    private static final By MOBILE_NUMBER_INPUT = By.id("mobile_number");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");

    public AccountInformationPage(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        super(driver, config, waitUtils);
    }

    public AccountInformationPage assertVisible() {
        waitUtils.untilUrlContains(driver, "/signup", config.explicitTimeout());
        visible(HEADING);
        visible(NAME_INPUT);
        visible(EMAIL_INPUT);
        visible(PASSWORD_INPUT);
        visible(CREATE_ACCOUNT_BUTTON);
        return this;
    }

    public AccountInformationPage selectTitle(Title title) {
        click(title.locator);
        return this;
    }

    public AccountInformationPage enterName(String name) {
        type(NAME_INPUT, name);
        return this;
    }

    public AccountInformationPage enterEmail(String email) {
        WebElement emailInput = visible(EMAIL_INPUT);
        String currentValue = emailInput.getAttribute("value");
        if (emailInput.isEnabled()) {
            emailInput.clear();
            emailInput.sendKeys(email);
            currentValue = emailInput.getAttribute("value");
        }

        if (!email.equals(currentValue)) {
            throw new IllegalStateException("Unexpected prefilled email in account information form.");
        }
        return this;
    }

    public AccountInformationPage enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public AccountInformationPage selectBirthDate(String day, String month, String year) {
        new Select(visible(DAYS_SELECT)).selectByValue(day);
        new Select(visible(MONTHS_SELECT)).selectByValue(month);
        new Select(visible(YEARS_SELECT)).selectByValue(year);
        return this;
    }

    public AccountInformationPage signUpForNewsletter() {
        ensureSelected(NEWSLETTER_CHECKBOX);
        return this;
    }

    public AccountInformationPage receivePartnerOffers() {
        ensureSelected(OFFERS_CHECKBOX);
        return this;
    }

    public AccountInformationPage fillAddressDetails(String firstName, String lastName, String company,
            String address1, String address2, String country, String state, String city, String zipcode,
            String mobileNumber) {
        type(FIRST_NAME_INPUT, firstName);
        type(LAST_NAME_INPUT, lastName);
        type(COMPANY_INPUT, company);
        type(ADDRESS_ONE_INPUT, address1);
        type(ADDRESS_TWO_INPUT, address2);
        selectOptionByVisibleTextResilient(COUNTRY_SELECT, country);
        type(STATE_INPUT, state);
        type(CITY_INPUT, city);
        type(ZIPCODE_INPUT, zipcode);
        type(MOBILE_NUMBER_INPUT, mobileNumber);
        return this;
    }

    public AccountCreatedPage submitCreateAccount() {
        scrollIntoView(CREATE_ACCOUNT_BUTTON);
        click(CREATE_ACCOUNT_BUTTON);
        return new AccountCreatedPage(driver, config, waitUtils);
    }

    public enum Title {

        MR(TITLE_MR),
        MRS(TITLE_MRS);

        private final By locator;

        Title(By locator) {
            this.locator = locator;
        }
    }

    private void ensureSelected(By locator) {
        WebElement element = visible(locator);
        if (!element.isSelected()) {
            click(locator);
        }
    }

    private void selectOptionByVisibleTextResilient(By locator, String visibleText) {
        WebElement selectElement = visible(locator);
        scrollIntoView(locator);
        try {
            new Select(selectElement).selectByVisibleText(visibleText);
        } catch (RuntimeException exception) {
            dismissBlockingInterferenceIfPresent();
            Boolean updated = (Boolean) executeScript(
                    "const select = arguments[0];"
                            + "const expectedText = arguments[1];"
                            + "const option = Array.from(select.options).find(item => item.text.trim() === expectedText);"
                            + "if (!option) return false;"
                            + "select.value = option.value;"
                            + "select.dispatchEvent(new Event('change', { bubbles: true }));"
                            + "return true;",
                    selectElement,
                    visibleText);
            if (!Boolean.TRUE.equals(updated)) {
                throw exception;
            }
        }
    }
}