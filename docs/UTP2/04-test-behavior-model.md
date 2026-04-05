# Test Behavior Model

## TestCases

`TestCases` capture intent; `TestProcedures` capture the executable steps performed by the framework.

| Scenario ID | Test Case | SUT Interface | Primary Procedure Elements | Current Status |
|---|---|---|---|---|
| `UI-SMK-001` | Home page smoke | `WebUserInterface` | `BaseUiTest`, `HomePage`, `HeaderComponent`, `CookieConsentComponent`, `UiAssertions` | Stable |
| `API-REG-001` | Products catalog API | `PublicRestApi` | `BaseApiTest`, `AutomationExerciseApiClient`, `ProductApiService`, `ApiAssertions` | Stable |
| `UI-REG-001` | Register new user | `WebUserInterface` | `BaseUiTest`, `SignupFlow`, `LoginPage`, `SignupPage`, `AccountCreatedPage` | Implemented regression path |
| `UI-REG-002` | Valid login | `WebUserInterface` | `BaseUiTest`, API user setup, `LoginFlow`, `HomePage` | Implemented regression path |
| `UI-REG-003` | Search product | `WebUserInterface` | `BaseUiTest`, `ProductsPage`, `UiAssertions` | Implemented regression path |
| `E2E-V1-001` | API-to-UI checkout | `PublicRestApi` + `WebUserInterface` | `BaseE2eTest`, API services, `CartFlow` | Placeholder, currently skipped |

## TestProcedures

### Home Page Smoke

1. Load `TestContext`.
2. Create browser session.
3. Open the home page and tolerate cookie consent.
4. Validate home page load, core navigation, hero visibility, and title.
5. Quit the browser and persist results.

### Products API

1. Load `TestContext`.
2. Create API request specification and client.
3. Call `/api/productsList`.
4. Parse the response into `ProductsResponse`.
5. Validate HTTP status, business response code, payload presence, and schema.

### Register User

1. Generate unique user data.
2. Open the login/signup area through the home page.
3. Execute `SignupFlow`.
4. Validate account creation confirmation and logged-in marker.
5. Attempt best-effort API cleanup.

### Valid Login

1. Generate a unique user.
2. Seed the user through the API.
3. Open the login page and execute `LoginFlow`.
4. Validate the logged-in marker.
5. Attempt best-effort API cleanup.

### Search Product

1. Open the products page.
2. Validate initial product visibility.
3. Search for a known product.
4. Validate searched-results visibility and product name presence.

### API-to-UI Checkout

1. Prepare cross-layer data and browser runtime.
2. Reuse API and UI components to reach checkout.
3. Keep the scenario explicitly skipped until the live checkout path is validated.

## Behavioral Positioning

- Smoke and products API define the current confidence baseline.
- Registration, login, and search are implemented regression procedures.
- Checkout remains a documented target V1 behavior, not a delivered stable procedure.
