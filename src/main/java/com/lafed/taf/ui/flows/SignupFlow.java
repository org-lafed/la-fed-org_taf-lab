package com.lafed.taf.ui.flows;

import com.lafed.taf.api.models.User;
import com.lafed.taf.ui.pages.AccountCreatedPage;
import com.lafed.taf.ui.pages.LoginPage;

public class SignupFlow {
    private final LoginPage loginPage;

    public SignupFlow(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public AccountCreatedPage register(User user) {
        return loginPage
                .enterSignupName(user.getName())
                .enterSignupEmail(user.getEmail())
                .startSignup()
                .fillAccountDetails(user)
                .submitAccountCreation();
    }
}