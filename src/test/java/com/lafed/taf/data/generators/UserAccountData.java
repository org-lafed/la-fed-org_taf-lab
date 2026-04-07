package com.lafed.taf.data.generators;

/**
 * Immutable UI account data for register/login scenarios.
 */
public record UserAccountData(
        String firstName,
        String lastName,
        String email,
        String password,
        String birthDay,
        String birthMonth,
        String birthYear,
        String company,
        String addressLine1,
        String addressLine2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber) {

    public String displayName() {
        return firstName + " " + lastName;
    }
}
