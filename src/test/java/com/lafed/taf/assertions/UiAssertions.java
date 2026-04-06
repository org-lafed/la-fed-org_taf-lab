package com.lafed.taf.assertions;

import org.testng.Assert;

/**
 * Generic UI assertion helpers.
 */
public final class UiAssertions {

    private UiAssertions() {
    }

    public static void assertTitleContains(String actualTitle, String expectedFragment) {
        Assert.assertNotNull(actualTitle, "Page title must not be null.");
        Assert.assertTrue(actualTitle.contains(expectedFragment),
                "Page title does not contain expected fragment: " + expectedFragment);
    }
}
