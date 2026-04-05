# Modeling Overview

This package documents the framework with two complementary views:

- UML diagrams under `docs/UML` for architecture, behavior, and execution.
- UTP2-oriented models under `docs/UTP2` for test architecture, context, data, verdicts, and traceability.

All diagrams are authored in PlantUML. The package is intentionally conservative: it documents the repository as it exists today, highlights the near-term V1 evolution, and keeps longer-term directions separate from implemented scope.

## As-Is Model

- The framework is layered into `config`, `core`, `ui`, `api`, `data`, and `tests`.
- Execution is driven by Maven and TestNG suite XML files.
- The most reliable demonstrator paths are the home page smoke flow and the products API flow.
- UI regression coverage is implemented for registration, valid login, and product search, but these remain broader regression scenarios rather than the core smoke baseline.
- The API-to-UI checkout path is present only as a compile-safe skipped placeholder while live checkout locator validation is still pending.
- Reporting currently produces standard `target/allure-results` and `target/surefire-reports`.

## Target V1 Model

- Treat the current layered architecture as the formal V1 baseline.
- Keep smoke and products API as the anchor portfolio scenarios.
- Preserve registration, login, and search as supported regression flows without presenting checkout as stabilized before the live path is hardened.
- Strengthen reusable flow modeling, scenario-state handoff, and objective-to-evidence traceability.
- Use the activity and deployment views as the reference execution model for local and CI runs.

## Target V2 Direction

- Introduce remote browser execution support for grid or cloud providers.
- Expand richer cross-layer scenarios once checkout and payment paths are validated against the live site.
- Mature test data lifecycle management, cleanup orchestration, and reporting metadata.
- Extend traceability beyond scenario-level mapping toward release and requirement views.

## File Map

- `01-context-diagram.puml`: system boundary, actors, and external interfaces.
- `02-package-diagram.puml`: layered package structure and dependencies.
- `03-use-case-diagram.puml`: framework execution use cases and tested business scenarios.
- `04-class-diagram-core.puml`: configuration, driver, state, and execution support.
- `05-class-diagram-ui-api.puml`: UI, API, assertion, and test-facing abstractions.
- `06-sequence-home-smoke.puml`: current stable smoke execution.
- `07-sequence-products-api.puml`: current stable API execution.
- `08-sequence-signup-flow.puml`: target V1 interaction model for reusable signup flow handling.
- `09-activity-suite-execution.puml`: local and CI suite execution behavior.
- `10-deployment-execution-view.puml`: execution nodes and artifact flow.
