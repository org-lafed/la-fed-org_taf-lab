package com.lafed.taf.ui.pages;

import com.lafed.taf.api.models.User;
import com.lafed.taf.config.ExecutionConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends BasePage {
    private static final By ACCOUNT_INFO_HEADER = By.cssSelector(".login-form h2 b");
    private static final By TITLE_MR = By.cssSelector("#id_gender1");
    private static final By TITLE_MRS = By.cssSelector("#id_gender2");
    private static final By PASSWORD_INPUT = By.cssSelector("input[data-qa='password']");
    private static final By DAYS_SELECT = By.cssSelector("select[data-qa='days']");
    private static final By MONTHS_SELECT = By.cssSelector("select[data-qa='months']");
    private static final By YEARS_SELECT = By.cssSelector("select[data-qa='years']");
    private static final By FIRST_NAME_INPUT = By.cssSelector("input[data-qa='first_name']");
    private static final By LAST_NAME_INPUT = By.cssSelector("input[data-qa='last_name']");
    private static final By COMPANY_INPUT = By.cssSelector("input[data-qa='company']");
    private static final By ADDRESS1_INPUT = By.cssSelector("input[data-qa='address']");
    private static final By ADDRESS2_INPUT = By.cssSelector("input[data-qa='address2']");
    private static final By COUNTRY_SELECT = By.cssSelector("select[data-qa='country']");
    private static final By STATE_INPUT = By.cssSelector("input[data-qa='state']");
    private static final By CITY_INPUT = By.cssSelector("input[data-qa='city']");
    private static final By ZIPCODE_INPUT = By.cssSelector("input[data-qa='zipcode']");
    private static final By MOBILE_INPUT = By.cssSelector("input[data-qa='mobile_number']");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");
    private static final By ACCOUNT_CREATED_HEADER = By.cssSelector("h2[data-qa='account-created']");

    public SignupPage(WebDriver driver, ExecutionConfig config) {
        super(driver, config);
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed(ACCOUNT_INFO_HEADER) || isDisplayed(ACCOUNT_CREATED_HEADER);
    }

    public SignupPage fillAccountDetails(User user) {
        if ("Mrs".equalsIgnoreCase(user.getTitle()) || "Miss".equalsIgnoreCase(user.getTitle())) {
            click(TITLE_MRS);
        } else {
            click(TITLE_MR);
        }

        type(PASSWORD_INPUT, user.getPassword());
        selectByVisibleText(DAYS_SELECT, user.getBirthDate());
        selectByVisibleText(MONTHS_SELECT, user.getBirthMonth());
        selectByVisibleText(YEARS_SELECT, user.getBirthYear());
        type(FIRST_NAME_INPUT, user.getFirstName());
        type(LAST_NAME_INPUT, user.getLastName());
        type(COMPANY_INPUT, user.getCompany());
        type(ADDRESS1_INPUT, user.getAddress1());
        type(ADDRESS2_INPUT, user.getAddress2());
        selectByVisibleText(COUNTRY_SELECT, user.getCountry());
        type(STATE_INPUT, user.getState());
        type(CITY_INPUT, user.getCity());
        type(ZIPCODE_INPUT, user.getZipcode());
        type(MOBILE_INPUT, user.getMobileNumber());
        return this;
    }

    public SignupPage submitAccountCreation() {
        // TODO: Validate the final account creation confirmation flow against the live site.
        click(CREATE_ACCOUNT_BUTTON);
        return this;
    }

    public boolean isAccountCreated() {
        return isDisplayed(ACCOUNT_CREATED_HEADER);
    }

    private void selectByVisibleText(By locator, String text) {
        new Select(find(locator)).selectByVisibleText(text);
    }
}
