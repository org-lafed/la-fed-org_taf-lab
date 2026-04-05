package com.lafed.taf.assertions;

import org.testng.Assert;

public final class UiAssertions {
    private UiAssertions() {
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void assertContains(String actual, String expectedFragment, String message) {
        Assert.assertTrue(actual != null && actual.contains(expectedFragment), message);
    }
}
