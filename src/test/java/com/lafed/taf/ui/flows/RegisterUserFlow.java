package com.lafed.taf.ui.flows;

import com.lafed.taf.config.ExecutionConfig;
import com.lafed.taf.core.utils.WaitUtils;
import com.lafed.taf.ui.guards.UiInterferenceGuard;
import com.lafed.taf.ui.pages.AccountCreatedPage;
import com.lafed.taf.ui.pages.AccountDeletedPage;
import com.lafed.taf.ui.pages.AccountInformationPage;
import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates Test Case 1: register a brand-new user and delete it afterwards.
 */
public final class RegisterUserFlow {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserFlow.class);

    private final WebDriver driver;
    private final ExecutionConfig config;
    private final WaitUtils waitUtils;
    private final UiInterferenceGuard interferenceGuard;
    private final RegisterUserProfile profile;

    public RegisterUserFlow(WebDriver driver, ExecutionConfig config, WaitUtils waitUtils) {
        this.driver = driver;
        this.config = config;
        this.waitUtils = waitUtils;
        this.interferenceGuard = new UiInterferenceGuard(driver, config, waitUtils);
        this.profile = RegisterUserProfile.generate();
    }

    public void execute() {
        LOG.info("[INFO] Browser={} | BaseUrl={}", config.browserName(), config.uiBaseUrl());

        HomePage homePage = protectedStep("Navigate to url 'http://automationexercise.com'", () -> {
            HomePage page = new HomePage(driver, config, waitUtils).open();
            page.acceptConsentIfPresent();
            return page;
        });
        step("Verify that home page is visible successfully", homePage::waitUntilReady);

        LoginPage loginPage = protectedStep("Click on 'Signup / Login' button", homePage::openSignupLogin);
        step("Verify 'New User Signup!' is visible", loginPage::assertNewUserSignupVisible);
        step("Enter name and email address", () -> loginPage
                .enterSignupName(profile.displayName())
                .enterSignupEmail(profile.email()));
        AccountInformationPage accountInformationPage = protectedStep("Click 'Signup' button", loginPage::submitNewUser);

        step("Verify that 'ENTER ACCOUNT INFORMATION' is visible", accountInformationPage::assertVisible);
        protectedStep("Fill details: Title, Name, Email, Password, Date of birth", () -> accountInformationPage
                .selectTitle(AccountInformationPage.Title.MR)
                .enterName(profile.displayName())
                .enterEmail(profile.email())
                .enterPassword(profile.password())
                .selectBirthDate(profile.birthDay(), profile.birthMonth(), profile.birthYear()));
        step("Select checkbox 'Sign up for our newsletter!'", accountInformationPage::signUpForNewsletter);
        step("Select checkbox 'Receive special offers from our partners!'", accountInformationPage::receivePartnerOffers);
        protectedStep("Fill details: First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile Number",
                () -> accountInformationPage.fillAddressDetails(
                        profile.firstName(),
                        profile.lastName(),
                        profile.company(),
                        profile.addressLine1(),
                        profile.addressLine2(),
                        profile.country(),
                        profile.state(),
                        profile.city(),
                        profile.zipcode(),
                        profile.mobileNumber()));

        AccountCreatedPage accountCreatedPage = protectedStep(
                "Click 'Create Account' button",
                accountInformationPage::submitCreateAccount);
        protectedAssertionStep(
                "Verify that 'ACCOUNT CREATED!' is visible",
                accountCreatedPage::assertVisible,
                accountInformationPage::submitCreateAccount);
        HomePage authenticatedHomePage = protectedStep("Click 'Continue' button", accountCreatedPage::clickContinue);

        protectedAssertionStep(
                "Verify that 'Logged in as username' is visible",
                () -> authenticatedHomePage.waitUntilAuthenticated(profile.displayName()),
                accountCreatedPage::clickContinue);
        AccountDeletedPage accountDeletedPage = protectedStep(
                "Click 'Delete Account' button",
                authenticatedHomePage::deleteAccount);
        protectedAssertionStep(
                "Verify that 'ACCOUNT DELETED!' is visible",
                accountDeletedPage::assertVisible,
                authenticatedHomePage::deleteAccount);
        protectedStep("Click 'Continue' button", accountDeletedPage::clickContinue);
    }

    private void step(String action, Runnable runnable) {
        LOG.info("[START] {}", action);
        try {
            runnable.run();
            LOG.info("[DONE] {}", action);
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }

    private <T> T step(String action, Supplier<T> supplier) {
        LOG.info("[START] {}", action);
        try {
            T result = supplier.get();
            LOG.info("[DONE] {}", action);
            return result;
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }

    private void protectedStep(String action, Runnable runnable) {
        step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                runnable.run();
                interferenceGuard.finalizeProtectedStep();
                return null;
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                runnable.run();
                interferenceGuard.finalizeProtectedStep();
                return null;
            }
        });
    }

    private <T> T protectedStep(String action, Supplier<T> supplier) {
        return step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                T result = supplier.get();
                interferenceGuard.finalizeProtectedStep();
                return result;
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                T result = supplier.get();
                interferenceGuard.finalizeProtectedStep();
                return result;
            }
        });
    }

    private void protectedAssertionStep(String action, Runnable assertion, Runnable transitionRetry) {
        step(action, () -> {
            interferenceGuard.prepareProtectedStep();
            try {
                assertion.run();
                interferenceGuard.finalizeProtectedStep();
            } catch (RuntimeException exception) {
                if (!interferenceGuard.isProbableInterference(exception)) {
                    throw exception;
                }
                interferenceGuard.recoverAfterInterference();
                interferenceGuard.logRetry();
                transitionRetry.run();
                interferenceGuard.finalizeProtectedStep();
                assertion.run();
                interferenceGuard.finalizeProtectedStep();
            }
        });
    }

    private static final class RegisterUserProfile {

        private final String firstName;
        private final String lastName;
        private final String displayName;
        private final String email;
        private final String password;
        private final String birthDay;
        private final String birthMonth;
        private final String birthYear;
        private final String company;
        private final String addressLine1;
        private final String addressLine2;
        private final String country;
        private final String state;
        private final String city;
        private final String zipcode;
        private final String mobileNumber;

        private RegisterUserProfile(String firstName, String lastName, String displayName, String email,
                String password, String birthDay, String birthMonth, String birthYear, String company,
                String addressLine1, String addressLine2, String country, String state, String city,
                String zipcode, String mobileNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.displayName = displayName;
            this.email = email;
            this.password = password;
            this.birthDay = birthDay;
            this.birthMonth = birthMonth;
            this.birthYear = birthYear;
            this.company = company;
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.country = country;
            this.state = state;
            this.city = city;
            this.zipcode = zipcode;
            this.mobileNumber = mobileNumber;
        }

        static RegisterUserProfile generate() {
            long suffix = System.currentTimeMillis() % 10000000L;
            String suffixText = Long.toString(suffix);
            String firstName = "Auto";
            String lastName = "Tester" + suffixText;
            String displayName = firstName + " " + lastName;
            String email = "auto.register." + suffixText + "@example.com";
            String password = "P@ssw0rd!" + suffixText;
            String birthDay = "10";
            String birthMonth = "4";
            String birthYear = "1990";
            String company = "QA Labs";
            String addressLine1 = "123 Automation Blvd";
            String addressLine2 = "Unit " + suffixText;
            String country = "United States";
            String state = "California";
            String city = "Los Angeles";
            String zipcode = "90001";
            String mobileNumber = "555" + String.format("%07d", suffix);
            return new RegisterUserProfile(
                    firstName,
                    lastName,
                    displayName,
                    email,
                    password,
                    birthDay,
                    birthMonth,
                    birthYear,
                    company,
                    addressLine1,
                    addressLine2,
                    country,
                    state,
                    city,
                    zipcode,
                    mobileNumber);
        }

        public String firstName() {
            return firstName;
        }

        public String lastName() {
            return lastName;
        }

        public String displayName() {
            return displayName;
        }

        public String email() {
            return email;
        }

        public String password() {
            return password;
        }

        public String birthDay() {
            return birthDay;
        }

        public String birthMonth() {
            return birthMonth;
        }

        public String birthYear() {
            return birthYear;
        }

        public String company() {
            return company;
        }

        public String addressLine1() {
            return addressLine1;
        }

        public String addressLine2() {
            return addressLine2;
        }

        public String country() {
            return country;
        }

        public String state() {
            return state;
        }

        public String city() {
            return city;
        }

        public String zipcode() {
            return zipcode;
        }

        public String mobileNumber() {
            return mobileNumber;
        }
    }
}
