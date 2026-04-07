package com.lafed.taf.tests.api;

import com.lafed.taf.api.assertions.ApiAssertions;
import com.lafed.taf.api.assertions.ProductsApiAssertions;
import com.lafed.taf.api.clients.ProductsApiClient;
import com.lafed.taf.api.models.ProductsListResponse;
import com.lafed.taf.api.services.ProductsApiService;
import com.lafed.taf.core.allure.AllureEnvironmentListener;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * API 1 executable test for retrieving the full products list.
 */
@Listeners({AllureTestNg.class, AllureEnvironmentListener.class})
public final class Api01GetAllProductsListTest extends BaseApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(Api01GetAllProductsListTest.class);

    private ProductsApiService productsApiService;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        initializeApiContext();
        this.productsApiService = new ProductsApiService(new ProductsApiClient(requestSpecFactory.create(config)));
    }

    @Test(groups = {"api", "api01"})
    public void shouldReturnANonEmptyProductsList() {
        Response response = runStep("Send GET /api/productsList", productsApiService::getAllProductsListResponse);
        ProductsListResponse payload = runStep("Parse products list JSON",
                () -> productsApiService.parseProductsListResponse(response));

        runStep("Assert HTTP status code", () -> ApiAssertions.assertStatusCode(response, 200));
        runStep("Assert response body is valid JSON", () -> ProductsApiAssertions.assertBodyIsValidJson(response));
        runStep("Assert payload responseCode", () -> ProductsApiAssertions.assertApplicationResponseCode(payload, 200));
        runStep("Assert products list is usable and non-empty",
                () -> ProductsApiAssertions.assertContainsUsableProducts(payload));
    }

    private <T> T runStep(String action, StepSupplier<T> supplier) {
        LOG.info("[START] {}", action);
        try {
            T result = supplier.get();
            LOG.info("[DONE] {}", action);
            return result;
        } catch (RuntimeException exception) {
            LOG.error("[FAIL] {}", action, exception);
            throw exception;
        }
    }

    private void runStep(String action, Runnable runnable) {
        runStep(action, () -> {
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    private interface StepSupplier<T> {
        T get();
    }
}
