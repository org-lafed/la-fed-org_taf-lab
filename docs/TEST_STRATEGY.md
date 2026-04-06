# Test Strategy

The first version focuses on a pragmatic balance between realism and framework clarity.

- `smoke`: fast confidence checks for core site availability and navigation.
- `api`: stable contract checks for public Automation Exercise API endpoints.
- `ui`: starter functional coverage for registration and cart journeys.
- `e2e`: API-to-UI scenarios that show cross-layer orchestration.

Principles:

- Keep smoke and product API tests as the most reliable demonstrators.
- Mark incomplete live-locator scenarios as explicit skips instead of shipping flaky failures.
- Use configuration-driven execution for local, demo, and CI environments.
