package com.lafed.taf.data.builders;

import com.lafed.taf.api.models.User;
import com.lafed.taf.data.generators.FakeDataGenerator;

public class UserDataBuilder {
    private final User user;

    private UserDataBuilder() {
        user = new User();
        user.setName("Demo User");
        user.setEmail(FakeDataGenerator.uniqueEmail("taf-user"));
        user.setPassword("ReplaceViaEnvOrSecretStore123");
        user.setTitle("Mr");
        user.setBirthDate("10");
        user.setBirthMonth("May");
        user.setBirthYear("1992");
        user.setFirstName("Demo");
        user.setLastName("User");
        user.setCompany("LA FED");
        user.setAddress1("123 Automation Street");
        user.setAddress2("Suite 10");
        user.setCountry("Canada");
        user.setZipcode("10001");
        user.setState("QC");
        user.setCity("Montreal");
        user.setMobileNumber("+15145550100");
    }

    public static UserDataBuilder aUser() {
        return new UserDataBuilder();
    }

    public UserDataBuilder withName(String name) {
        user.setName(name);
        return this;
    }

    public UserDataBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserDataBuilder withPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public User build() {
        return user;
    }
}
