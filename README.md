# Test Automation Framework implemented above the TAE website for demonstration purposes.

Minimal Java Maven Test Automation Framework for demonstration on [automationexercise.com](https://automationexercise.com). The project is intentionally layered, readable, and ready for future expansion.

## Current Scope

- Stable demonstrator coverage for home page smoke, signup happy path, valid login, product list visibility, product search, and the products API path.
- Checkout-oriented E2E remains an explicit placeholder until live checkout locator validation is completed.
- Build output and local dependency caches are intentionally ignored and are not part of the versioned source.

## Purpose

- Demonstrate a clean UI, API, and E2E automation framework structure.
- Keep the first version compilable and easy to extend.
- Produce standard Allure result files under `target/allure-results`.

## Stack

- Java 17
- Maven
- TestNG
- Selenium 4
- RestAssured
- Jackson
- SLF4J + Logback
- Allure TestNG
- WebDriverManager

## Architecture Overview

- `config`: environment-driven execution configuration.
- `core/driver`: browser lifecycle and driver creation.
- `core/http`: shared API client factory and request handling.
- `core/listeners`: Allure listener and reporting support.
- `core/utils`: waits, screenshots, and resource helpers.
- `core/state`: lightweight cross-step state storage.
- `ui/pages`, `ui/components`, `ui/flows`: page object layer and business flows.
- `api/clients`, `api/services`, `api/models`, `api/assertions`: API abstraction and validations.
- `data/builders`, `data/generators`: test data helpers.
- `tests/ui`, `tests/api`, `tests/e2e`: runnable TestNG tests only.

See the concise project documentation in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) and related files.

## How To Run

Smoke suite:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/smoke.xml -Denv=local
```

UI regression:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/ui-regression.xml -Denv=local
```

API regression:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/api-regression.xml -Denv=demo
```

Full regression:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suites/full-regression.xml -Denv=ci
```

Useful environment variable overrides:

- `TAF_ENV`
- `TAF_BASE_URL`
- `TAF_API_BASE_URL`
- `TAF_BROWSER`
- `TAF_HEADLESS`
- `TAF_EXPLICIT_TIMEOUT_SECONDS`
- `TAF_SCREENSHOT_ON_FAILURE`

## Reports

- Allure raw results: `target/allure-results`
- Surefire reports: `target/surefire-reports`

The framework intentionally emits standard `allure-results` so a newer Allure Report renderer can be plugged in later without changing test code.