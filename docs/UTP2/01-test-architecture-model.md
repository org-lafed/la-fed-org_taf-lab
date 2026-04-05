# Test Architecture Model

## Architectural Intent

The framework acts as a small test system around two SUT interfaces:

- `WebUserInterface`: browser-driven interaction with Automation Exercise pages.
- `PublicRestApi`: RestAssured-driven interaction with the public API.

The current design keeps reusable automation code outside the `tests` layer and limits test classes to orchestration and assertions.

## UTP2 Mapping

| UTP2 Concept | Repository Mapping | Current Notes |
|---|---|---|
| `TestContext` | `ConfigManager`, `ExecutionConfig`, suite parameters, environment variables | Implemented and used by all suites |
| `TestComponent` | `BasePage`, page objects, components, flows, API client, services, assertions | Implemented |
| `TestComponent` | `BaseUiTest`, `BaseApiTest`, `BaseE2eTest` | Implemented as test scaffolding |
| `SUT Interface` | Browser-facing web UI | Implemented |
| `SUT Interface` | Public REST API | Implemented |
| `TestController` | Maven + TestNG + suite XML files | Implemented |
| `Scheduler` | Local CLI execution, GitHub Actions workflow execution | Implemented at execution level |
| `TestArtifact` | `allure-results`, `surefire-reports`, screenshots, API attachments | Implemented |

## Test Components

| Logical Component | Concrete Repository Elements | Responsibility |
|---|---|---|
| `ConfigAdapter` | `ConfigManager`, `ExecutionConfig` | Resolve execution environment and runtime properties |
| `UIAdapter` | `DriverFactory`, `DriverManager`, `BasePage`, page objects, UI flows | Drive and observe the web UI |
| `APIAdapter` | `ApiClientFactory`, `AutomationExerciseApiClient`, services | Drive and observe REST endpoints |
| `StateAdapter` | `StateStore` | Hold cross-step state when a scenario needs it |
| `ReportingAdapter` | `AllureTestListener`, `AllureSupport`, `ScreenshotService`, API attachments | Preserve evidence and result metadata |

## As-Is vs Target V1

- As-is, the architecture already supports UI, API, and starter E2E scaffolding.
- V1 formalizes this structure and its traceability rather than changing the layer boundaries.
- Checkout-oriented cross-layer reuse remains architectural intent until the live checkout path is validated.
