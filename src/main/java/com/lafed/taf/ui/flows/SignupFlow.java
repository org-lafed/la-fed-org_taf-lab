package com.lafed.taf.ui.flows;

import com.lafed.taf.api.models.User;
import com.lafed.taf.ui.pages.LoginPage;
import com.lafed.taf.ui.pages.SignupPage;

public class SignupFlow {
    private final LoginPage loginPage;

    public SignupFlow(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public SignupPage register(User user) {
        return loginPage
                .enterSignupName(user.getName())
                .enterSignupEmail(user.getEmail())
                .startSignup()
                .fillAccountDetails(user)
                .submitAccountCreation();
    }
}
