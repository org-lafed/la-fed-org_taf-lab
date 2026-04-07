package com.lafed.taf.data.generators;

/**
 * Generates coherent unique account data for relaunchable UI scenarios.
 */
public final class UserAccountDataGenerator {

    public UserAccountData generate() {
        long suffix = System.currentTimeMillis() % 1_000_000_000L;
        String suffixText = String.format("%09d", suffix);

        return new UserAccountData(
                "Auto",
                "Login" + suffixText.substring(4),
                "auto.tc02." + suffixText + "@example.com",
                "Tc02Login!" + suffixText.substring(3),
                "10",
                "4",
                "1990",
                "QA Labs",
                "123 Automation Blvd",
                "Suite " + suffixText.substring(5),
                "United States",
                "California",
                "Los Angeles",
                suffixText.substring(0, 5),
                "555" + suffixText.substring(2));
    }
}
