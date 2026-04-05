# Reporting

The framework emits standard Allure result files into `target/allure-results`.

- `allure.properties` sets the default results directory.
- `categories.json` is copied into the results directory at runtime for defect grouping.
- UI failures attach browser screenshots to the Allure test entry.
- API requests and responses are attached through a RestAssured filter.

Primary output locations:

- `target/allure-results`
- `target/surefire-reports`
