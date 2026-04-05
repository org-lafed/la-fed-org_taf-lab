# Test Context Model

## TestContext

`TestContext` is the execution context resolved before tests interact with the SUT. In the repository it is represented primarily by `ExecutionConfig`, which is loaded through `ConfigManager`.

| Context Attribute | Repository Source | Purpose |
|---|---|---|
| `env` | `-Denv`, `TAF_ENV`, `application-<env>.properties` | Select local, demo, or CI configuration |
| `baseUrl` | properties or `TAF_BASE_URL` | Target web UI endpoint |
| `apiBaseUrl` | properties or `TAF_API_BASE_URL` | Target API endpoint |
| `browser` | properties or `TAF_BROWSER` | Browser type for UI suites |
| `headless` | properties or `TAF_HEADLESS` | UI execution mode |
| `windowWidth`, `windowHeight` | properties | Browser viewport control |
| `explicitTimeoutSeconds` | properties or env override | Wait utility behavior |
| `pageLoadTimeoutSeconds` | properties or env override | Page load timeout behavior |
| `screenshotOnFailure` | properties or env override | UI failure evidence policy |

## Context Resolution Rules

- System properties have priority over environment variables and file-based defaults.
- Environment-specific property files live under `src/test/resources/config`.
- `ConfigManager` trims trailing slashes and returns an immutable `ExecutionConfig`.

## Runtime Context

Runtime context is narrower than execution configuration:

- `DriverManager` holds the active browser in thread-local scope.
- `StateStore` holds scenario state in thread-local scope and is cleared in UI teardown.
- API request specification is created per test fixture through `ApiClientFactory`.

## As-Is vs Target V1

- As-is, `TestContext` is sufficient for local and CI execution.
- V1 can rely more explicitly on `StateStore` for cross-layer procedure handoff.
- V2 can extend context toward remote execution capabilities and richer environment metadata.
