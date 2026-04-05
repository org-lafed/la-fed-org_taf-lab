# UTP2-Oriented Overview

This package maps the repository onto a pragmatic UML Testing Profile 2 style vocabulary. It is intentionally implementation-oriented rather than a formal standards repository.

## Scope

- Model the current test architecture, execution context, data handling, behavior, verdicts, and traceability.
- Keep stable demonstrator paths explicit: home page smoke and products API.
- Represent registration, login, and product search as implemented regression scenarios.
- Keep API-to-UI checkout clearly marked as a target V1 scenario whose current test class is still a skipped placeholder.

## UTP2 View Used Here

- `TestContext`: execution configuration and runtime context.
- `TestComponents`: framework parts that stimulate, observe, and report on the SUT.
- `SUT Interfaces`: the web UI and public REST API.
- `TestData`: static resources, generated data, and shared scenario state.
- `TestCases` and `TestProcedures`: scenario intent and executable flow.
- `Verdicts` and `Evidence`: how outcomes are assigned and preserved.
- `Traceability`: links from objectives to tests, suites, and artifacts.

## Reading Order

- `01-test-architecture-model.md`: component mapping and system boundary.
- `02-test-context-model.md`: configuration and runtime context.
- `03-test-data-model.md`: data sources and scenario state.
- `04-test-behavior-model.md`: test cases, procedures, and execution semantics.
- `05-verdict-and-reporting-model.md`: verdict assignment and evidence flow.
- `06-traceability-matrix.md`: objective-to-implementation mapping.
