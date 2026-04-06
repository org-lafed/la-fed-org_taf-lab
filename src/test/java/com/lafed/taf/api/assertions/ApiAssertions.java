package com.lafed.taf.api.assertions;

import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Generic API assertion helpers.
 */
public final class ApiAssertions {

    private ApiAssertions() {
    }

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        Assert.assertNotNull(response, "Response must not be null.");
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Unexpected HTTP status code.");
    }
}
