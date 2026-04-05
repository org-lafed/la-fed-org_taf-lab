# Traceability Matrix

| Test Objective | Scenario ID | Layer | Current Status | Test Class | Main Procedure / Service | Data Source | Primary Suite | Evidence | Notes |
|---|---|---|---|---|---|---|---|---|---|
| Verify home page availability and core navigation | `UI-SMK-001` | UI | Stable | `HomePageSmokeTest` | `HomePage`, `HeaderComponent`, `CookieConsentComponent` | None | `smoke.xml` | Allure result, optional failure screenshot | Also included through UI and full regression aggregation |
| Verify the public products endpoint returns a valid catalog | `API-REG-001` | API | Stable | `GetProductsApiTest` | `ProductApiService`, `ApiAssertions` | `products-list-schema.json` | `api-regression.xml` | Allure request/response attachments, Surefire report | Current API baseline path |
| Verify user registration through the happy path | `UI-REG-001` | UI | Implemented regression path | `RegisterUserTest` | `SignupFlow`, `LoginPage`, `SignupPage`, `AccountCreatedPage` | Generated `User` data | `ui-regression.xml` | Allure result, optional failure screenshot | Cleanup is best-effort through API |
| Verify valid login for a seeded user | `UI-REG-002` | UI | Implemented regression path | `ValidLoginTest` | API user setup, `LoginFlow`, `HomePage` | Generated `User` data | `ui-regression.xml` | Allure result, optional failure screenshot | Uses API setup to avoid dependency on existing accounts |
| Verify product search through the UI | `UI-REG-003` | UI | Implemented regression path | `SearchProductTest` | `ProductsPage`, `UiAssertions` | Inline search term (`Blue Top`) | `ui-regression.xml` | Allure result, optional failure screenshot | Regression coverage, not smoke baseline |
| Verify API-to-UI checkout entry path | `E2E-V1-001` | E2E | Placeholder, skipped | `ApiToUiCheckoutTest` | `BaseE2eTest`, API services, `CartFlow` | `account-checkout.json` target data | `full-regression.xml` | Skip record in Allure/Surefire | Awaiting live checkout locator validation |
