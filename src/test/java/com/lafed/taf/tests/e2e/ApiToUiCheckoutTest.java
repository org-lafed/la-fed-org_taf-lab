package com.lafed.taf.tests.e2e;

import org.testng.SkipException;
import org.testng.annotations.Test;

public class ApiToUiCheckoutTest extends BaseE2eTest {
    @Test(groups = {"e2e"}, description = "Starter API-to-UI checkout flow kept compile-safe until live locator validation is finished.")
    public void shouldSeedDataAndReachCheckoutFromUi() {
        throw new SkipException("TODO: locator validation is still required for the live checkout and payment path on automationexercise.com.");
    }
}
