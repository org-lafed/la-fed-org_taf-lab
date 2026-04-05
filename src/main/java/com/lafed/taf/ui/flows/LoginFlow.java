package com.lafed.taf.ui.flows;

import com.lafed.taf.ui.pages.HomePage;
import com.lafed.taf.ui.pages.LoginPage;

public class LoginFlow {
    private final LoginPage loginPage;

    public LoginFlow(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public HomePage login(String email, String password) {
        return loginPage
                .enterLoginEmail(email)
                .enterLoginPassword(password)
                .submitLogin();
    }
}
