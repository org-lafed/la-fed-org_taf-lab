# Architecture

This framework uses a small layered design aimed at readability and maintainability.

- `config` loads environment-specific properties and environment variable overrides.
- `core` contains browser, HTTP, listener, utility, and state infrastructure.
- `ui` contains page objects, reusable components, and flows that model business actions.
- `api` contains endpoint clients, services, DTOs, and API-focused assertions.
- `data` contains builders and generators for deterministic or synthetic test data.
- `tests` contains executable TestNG tests only.
- Shared UI interactions include a site-wide interference guard that mitigates cookie-adjacent overlays, ad iframes, and vignette-style page interference before and during user actions.

Design rules:

- Page objects expose interactions and state, not test assertions.
- Flows coordinate multiple pages/components.
- Test classes own assertions and business intent.
- Framework code is reusable from UI, API, and E2E suites.
